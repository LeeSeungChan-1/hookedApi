package kr.hooked.api.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Log4j2
public class JWTUtil {

//    private static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 토큰 생성, 검증에 필요한 키 랜덤 생성

//@@@ 키 고정(개발용)
    private static SecretKey secretKey;

    static {
        try {
            secretKey = Keys.hmacShaKeyFor("1234567890123456789012345678901234567890".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
//@@@

    public static int defaultAccessTokenExpireMinutes = 60; // 기본 엑세스 토큰 만료시간(분)
    public static int defaultRefreshTokenExpireMinutes = 6 * 60; // 기본 리프레쉬 토큰 만료시간(분)

    public static String generateToken(Map<String, Object> valueMap, int min) {

        String jwtStr = Jwts.builder() // 빌더 초기화
                .setHeader(Map.of("typ", "JWT")) // 헤더설정 토큰 타입을 JWT로 명시
                .setClaims(valueMap) // valueMap을 토큰의 클레임으로 설정
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 토큰의 발생 시간을 현재로 설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 토큰의 만료 시간을 min분 후로 설정
                .signWith(secretKey) // 키를 통해 토큰에 서명
                .compact(); // jwt 문자열 생성

        return jwtStr; // jwt 문자열 반환
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claims = null;

        try{
            claims = Jwts.parserBuilder() // jwt 파서 생성
                    .setSigningKey(secretKey) // 검증을 위한 키 설정
                    .build() // 파서 생성
                    .parseClaimsJws(token) // 토큰을 파싱하여 서명을 검증 및 jws 객체 반환
                    .getBody(); // 파싱된 토큰에서 클레임을 추출
        }catch (ExpiredJwtException expiredJwtException) { // 토큰 만료
            throw new CustomJWTException("ExpiredJwtException");
        }
//        catch (MalformedJwtException malformedJwtException){ // 토큰 형식 이상
//            throw new CustomJWTException("MalformedJwtException");
//        }catch (InvalidClaimException invalidClaimException){ // 토큰 클레임 값 이상
//            throw new CustomJWTException("InvalidClaimException");
//        }
        catch (JwtException jwtException){ // 기타 jwt 관련 예외
            throw new CustomJWTException("JwtException");
        }catch (Exception e){ // 다른 모든 예외
            throw new CustomJWTException("Exception");
        }
        return claims;
    }
}
