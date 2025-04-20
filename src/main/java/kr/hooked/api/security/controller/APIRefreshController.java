package kr.hooked.api.controller;

import kr.hooked.api.util.CustomJWTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @PostMapping("/api/empoyee/refresh")
    public Map<String, Object> refreshAccessToken(@RequestHeader("Authorization") String authorization, String refreshToken) {

        if(refreshToken == null || refreshToken.isEmpty()) {
            throw new CustomJWTException("NULL_REFRESH_TOKEN");
        }

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CustomJWTException("INVALID_AUTHORIZATION");
        }

        String accessToken = authorization.substring(7);






        return null;
    }

}
