package kr.hooked.api.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler { // Controller @PreAuthorize 권한이 없는 경우
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Gson gson = new Gson();

        String message = gson.toJson(Map.of("error", "ERROR_ACCESSDENIED"));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value()); // 상태값 403 설정

        PrintWriter printWriter = response.getWriter();
        printWriter.println(message);
        printWriter.close();
    }
}
