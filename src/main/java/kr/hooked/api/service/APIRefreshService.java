package kr.hooked.api.service;

import kr.hooked.api.dto.TokenDto;
import kr.hooked.api.util.CustomJWTException;
import kr.hooked.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class APIRefreshService {

    public Map<String, String> refreshAccessToken(TokenDto tokenDto) {

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        if(checkExpiredToken(accessToken) == false) { // 엑세스 토큰이 만료되기 이전이라면
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken); // 다시 반환
        }

        Map<String, Object> claims = JWTUtil.validateToken(refreshToken); // 엑세스 토큰이 만료 -> 리프레쉬 토큰 확인

        String newAccessToken = JWTUtil.generateToken(claims, JWTUtil.defaultAccessTokenExpireMinutes); // 만료된 엑세스 토큰 새로 발급

        // 리프레쉬 토큰이 만료 1시간 이내인 경우 새로 발급 -> 리프레쉬 토큰은 만료되면 재발급 x
//        String newRefreshToken =
//                checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, JWTUtil.defaultRefreshTokenExpireMinutes) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", refreshToken); // 새로 발급받은 토큰 반환
    }

//    private boolean checkTime(Integer exp){ // 리프레쉬 토큰이 만료 1시간 이내로 남은 경우 참
//
//        java.util.Date expDate = new java.util.Date((long) exp * 1000); // jwt exp를 날짜로 변환
//
//        long gap = expDate.getTime() - System.currentTimeMillis(); // 현재 시간과의 차이 계산 - 1000분의 1초
//
//        long leftMin = gap / (60 * 1000); // 60분
//
//        return leftMin < 60; // 1시간 이하 인지
//    }

    private boolean checkExpiredToken(String token){ // 토큰이 만료되었는지 체크
        try{
            JWTUtil.validateToken(token); // 토큰 검사
        }catch (CustomJWTException e){
            if(e.getMessage().equals("ExpiredJwtException")){ // 검사 결과가 만료되었을 때만 참
                return true;
            }
        }
        return false;
    }
}
