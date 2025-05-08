package kr.hooked.api.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hooked.api.dto.EmployeeSecurityDto;
import kr.hooked.api.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter { // OncePerRequestFilter : 모든 요청에 대해서 동작


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException { // 검사하지 않는 경우
//        // false : 검사 o // true : 검사 x
//        if(request.getMethod().equals("OPTIONS")){// Preflight요청은 체크하지 않음
//            return true;
//        }
//
        String path = request.getRequestURI(); // 요청 경로 가져오기
//
//        if(path.startsWith("/api/employee/login")) { // 로그인 경로의 호출은 체크하지 않음
//            return true;
//        }
//
        if(path.startsWith("/api/employee/")) { // 회원가입 경로의 호출은 체크하지 않음
            log.info(path);
            return true;
        }
//
//        if(path.startsWith("/api/token/refresh")) { // 토큰 재발급 호출은 체크하지 않음
//            return true;
//        }
//
//        if(path.startsWith("/actuator")) { // 상태 검사 주소는 체크하지 않음
//            return true;
//        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String authorizationHeader = request.getHeader("Authorization"); // 헤더에서 Authorization 가져오기

            if(authorizationHeader == null || authorizationHeader.isEmpty()) {
                throw new RuntimeException("Authorization header is empty");
            }
            String accessToken = authorizationHeader.substring(7); // "Bearer " 7자리 뒤 access 토큰 가져오기

            Map<String, Object> claims = JWTUtil.validateToken(accessToken); // 토큰 검증 -> 클레임 반환

            String username = (String) claims.get("username");
            String password = (String) claims.get("password");
            String name = (String) claims.get("name");
            String department = (String) claims.get("department");
            String position = (String) claims.get("position");
            List<String> authorityList = (List<String>) claims.get("authorityList");

            EmployeeSecurityDto employeeSecurityDto = new EmployeeSecurityDto(username, password, name, department, position, authorityList);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  =
                    new UsernamePasswordAuthenticationToken(employeeSecurityDto, password, employeeSecurityDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response); // 검사 후 원래 요청 실행
        }catch (Exception e){
            Gson gson = new Gson();
            String message = gson.toJson(Map.of("error", e.getMessage()));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(message);
            printWriter.close();
        }
    }
}
