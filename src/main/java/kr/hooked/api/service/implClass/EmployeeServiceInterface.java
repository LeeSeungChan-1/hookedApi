package kr.hooked.api.service.implClass;

import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmployeeServiceInterface {
    EmployeeResponseDto insert(EmployeeRequestDto employeeRequestDto);
    PageResponseDto<EmployeeResponseDto> selectAll(PageRequestDto pageRequestDto);
    EmployeeResponseDto updatePassword(PasswordRequestDto passwordRequestDto);
    EmployeeResponseDto select(long employeeId);
    Map<String, String> duplicationCheck(EmployeeRequestDto employeeRequestDto);
    Map<String, String> passwordCheck(PasswordRequestDto passwordRequestDto);
    Map<String, String> existsById(Long id);
}
