package kr.hooked.api.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hooked.api.security.dto.EmployeeSecurityDto;
import kr.hooked.api.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        EmployeeSecurityDto employeeSecurityDto = (EmployeeSecurityDto) authentication.getPrincipal();

        Map<String, Object> claims = employeeSecurityDto.getClaims();

        String accessToken = JWTUtil.generateToken(claims, JWTUtil.defaultAccessTokenExpireMinutes); // 기본 10분
        String refreshToken = JWTUtil.generateToken(claims, JWTUtil.defaultRefreshTokenExpireMinutes); // 기본 3시간

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims); // json 타입으로 변경

        response.setContentType("application/json; charset=UTF-8"); // json, utf-8 타입지정

        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonStr); // json 문자열 출력
        printWriter.close();
    }
}
