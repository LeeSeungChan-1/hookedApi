package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.PositionResponseDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.PositionRequestDto;
import kr.hooked.api.service.PositionService;
import kr.hooked.api.util.ValidCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/position")
@Log4j2
public class PositionController {
    private final PositionService positionService;

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/{id}")
    public ResponseEntity<?> select(@PathVariable Long id) {
        Map<String, String> positionCheck = positionService.existsById(id);
        if(!positionCheck.isEmpty()) {
            return ResponseEntity.badRequest().body(positionCheck);
        }
        PositionResponseDto result = positionService.select(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/list")
    public ResponseEntity<?> selectAll(PageRequestDto pageRequestDto) {
        PageResponseDto<PositionResponseDto> result = positionService.selectAll(pageRequestDto);

        if(result.getCurrentPage() > result.getTotalPage()) { // 요청한 페이지의 번호가 총 페이지 번호보다 높은 경우
            pageRequestDto.setPage(result.getTotalPage()); // 마지막 페이지로 이동
            result = positionService.selectAll(pageRequestDto);
//            return ResponseEntity.badRequest().body(Map.of("Message", "페이지를 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PostMapping("/")
    public ResponseEntity<?> insert(@RequestBody @Valid PositionRequestDto positionRequestDto, BindingResult bindingResult) {
        Map<String, String> validResult = ValidCheck.validCheck(bindingResult); // 입력값 검증
        if(validResult != null) { // 입력값에 문제가 있을 경우
            return ResponseEntity.badRequest().body(validResult);
        }
        Map<String, String> duplicationCheck = positionService.duplicationCheck(positionRequestDto);
        if(!duplicationCheck.isEmpty()) {
            return ResponseEntity.badRequest().body(duplicationCheck);
        }

        PositionResponseDto result = positionService.insert(positionRequestDto);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PositionRequestDto positionRequestDto, BindingResult bindingResult, @PathVariable Long id) {
        Map<String, String> validResult = ValidCheck.validCheck(bindingResult); // 입력값 검증
        if(validResult != null) { // 입력값에 문제가 있을 경우
            return ResponseEntity.badRequest().body(validResult);
        }
        Map<String, String> duplicationCheck = positionService.duplicationCheck(positionRequestDto);
        if(!duplicationCheck.isEmpty()) { // 유니크 값 검사
            return ResponseEntity.badRequest().body(duplicationCheck);
        }
        Map<String, String> positionCheck = positionService.existsById(id);
        if(!positionCheck.isEmpty()) { // 직책 조회
            return ResponseEntity.badRequest().body(positionCheck);
        }

        positionRequestDto.setPositionId(id);
        PositionResponseDto result = positionService.update(positionRequestDto);

        return ResponseEntity.ok(result);
    }

}
