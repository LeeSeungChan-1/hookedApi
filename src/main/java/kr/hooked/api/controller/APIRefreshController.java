package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.TokenDto;
import kr.hooked.api.service.APIRefreshService;
import kr.hooked.api.util.ValidCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    private final APIRefreshService apiRefreshService;

    @PostMapping("/api/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid TokenDto tokenDto, BindingResult bindingResult) {
        Map<String, String> validResult = ValidCheck.validCheck(bindingResult); // 입력값 검증

        if(!validResult.isEmpty()) { // 입력값에 문제가 있을 경우
            return ResponseEntity.badRequest().body(validResult);
        }

        Map<String, String> result = apiRefreshService.refreshAccessToken(tokenDto);

        return ResponseEntity.ok(result);

    }

}


