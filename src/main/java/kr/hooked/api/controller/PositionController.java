package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.PositionResponseDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.PositionRequestDto;
import kr.hooked.api.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/position")
@Log4j2
public class PositionController {
    private final PositionService positionService;

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/{id}")
    public ResponseEntity<?> select(@PathVariable Long id) {
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
    public ResponseEntity<?> insert(@RequestBody @Valid PositionRequestDto positionRequestDto) {
        PositionResponseDto result = positionService.insert(positionRequestDto);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PositionRequestDto positionRequestDto, @PathVariable Long id) {
        positionRequestDto.setId(id);
        PositionResponseDto result = positionService.update(positionRequestDto);

        return ResponseEntity.ok(result);
    }
}
