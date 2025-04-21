package kr.hooked.api.controller;

import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.reponse.PasswordResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import kr.hooked.api.service.DepartmentService;
import kr.hooked.api.service.EmployeeService;
import kr.hooked.api.service.PositionService;
import kr.hooked.api.util.FileUtil;
import kr.hooked.api.util.ValidCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employee")
@Log4j2
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final FileUtil fileUtil;

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @PostMapping("/")
    public ResponseEntity<?> insert(@Valid EmployeeRequestDto employeeRequestDto, BindingResult bindingResult) {
        Map<String, String> validCheck = ValidCheck.validCheck(bindingResult); // @Valid 를 통한 입력값 검증
        if (!validCheck.isEmpty()) { // 입력된 값이 문제가 있으면
            return ResponseEntity.badRequest().body(validCheck);
        }

        Map<String, String> duplicationCheck = employeeService.duplicationCheck(employeeRequestDto); // 유니크 항목 검증
        if(!duplicationCheck.isEmpty()) { // 중복된 값이 있으면
            return ResponseEntity.badRequest().body(duplicationCheck);
        }

        Map<String, String> fileCheck = fileUtil.validateEmployeeImage(employeeRequestDto.getEmployeeImage()); // 사진 검증
        if(!fileCheck.isEmpty()) { // 사진이 있고 조건에 맞지 않으면
            return ResponseEntity.badRequest().body(fileCheck);
        }

        Map<String, String> departmentCheck = departmentService.existsById(employeeRequestDto.getDepartmentId()); // 부서 검증
        if(!departmentCheck.isEmpty()) {
            return ResponseEntity.badRequest().body(departmentCheck);
        }

        Map<String, String> positionCheck = positionService.existsById(employeeRequestDto.getPositionId()); // 직책 검증
        if(!positionCheck.isEmpty()) {
            return ResponseEntity.badRequest().body(positionCheck);
        }

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
    public ResponseEntity<?> updatePassword(@Valid PasswordRequestDto passwordRequestDto, BindingResult bindingResult) {
        Map<String, String> validResult = ValidCheck.validCheck(bindingResult); // @Valid 를 통한 입력값 검증
        if (!validResult.isEmpty()) { // 입력된 값이 문제가 있으면
            return ResponseEntity.badRequest().body(validResult);
        }

        Map<String, String> checkResult = employeeService.passwordCheck(passwordRequestDto); // 사번으로 조회한 비밀번호와 입력된 비밀번호가 다르면
        if(!checkResult.isEmpty()) { // 입력된 값이 문제가 있으면
            return ResponseEntity.badRequest().body(checkResult);
        }

        EmployeeResponseDto result = employeeService.updatePassword(passwordRequestDto);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_dvAll')")
    @GetMapping("/read/{employeeId}")
    public ResponseEntity<?> select(@PathVariable long employeeId) {
        Map<String, String> employeeCheck = employeeService.existsById(employeeId);
        if(!employeeCheck.isEmpty()) { // 사원이 없으면
            return ResponseEntity.badRequest().body(employeeCheck);
        }

        EmployeeResponseDto result = employeeService.select(employeeId);
        return ResponseEntity.ok(result);
    }

}
