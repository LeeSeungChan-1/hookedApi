package kr.hooked.api.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class APILoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 상태코드 200으로 로그인 에러 메시지 전송
        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("error", "ERROR_LOGIN"));

        response.setContentType("application/json;");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonStr);
        printWriter.close();
    }
}
