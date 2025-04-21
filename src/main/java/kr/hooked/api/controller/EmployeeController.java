package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.EmployeeUpdateRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import kr.hooked.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employee")
@Log4j2
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PostMapping("/")
    public ResponseEntity<?> insert(@Valid EmployeeRequestDto employeeRequestDto) {
        EmployeeResponseDto result = employeeService.insert(employeeRequestDto);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/list")
    public ResponseEntity<?> selectAll(PageRequestDto pageRequestDto) {
        PageResponseDto<EmployeeResponseDto> result = employeeService.selectAll(pageRequestDto);
        if(result.getCurrentPage() > result.getTotalPage()) { // 요청한 페이지의 번호가 총 페이지 번호보다 높은 경우
            pageRequestDto.setPage(result.getTotalPage()); // 마지막 페이지로 이동
            result = employeeService.selectAll(pageRequestDto);
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid PasswordRequestDto passwordRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
        EmployeeResponseDto result = employeeService.updatePassword(passwordRequestDto, userDetails);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<?> select(@PathVariable long employeeId) {

        EmployeeResponseDto result = employeeService.select(employeeId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid EmployeeUpdateRequestDto employeeUpdateRequestDto, @PathVariable Long id) {
        employeeUpdateRequestDto.setId(id);

        EmployeeResponseDto result = employeeService.update(employeeUpdateRequestDto);

        return ResponseEntity.ok(result);
    }

}
