package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.DepartmentResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.DepartmentRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        DepartmentResponseDto result = departmentService.findById(id);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/list")
    public ResponseEntity<?> findAll(PageRequestDto pageRequestDto) {
        PageResponseDto<DepartmentResponseDto> result = departmentService.selectAll(pageRequestDto);
        if(result.getCurrentPage() > result.getTotalPage()) { // 요청한 페이지의 번호가 총 페이지 번호보다 높은 경우
            pageRequestDto.setPage(result.getTotalPage()); // 마지막 페이지로 이동
            result = departmentService.selectAll(pageRequestDto);
        }

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PostMapping("/")
    public ResponseEntity<?> insert(@Valid DepartmentRequestDto departmentRequestDto){
        DepartmentResponseDto result = departmentService.insert(departmentRequestDto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid DepartmentRequestDto departmentRequestDto){
        departmentRequestDto.setId(id);
        DepartmentResponseDto result = departmentService.update(departmentRequestDto);
        return ResponseEntity.ok(result);
    }
}
