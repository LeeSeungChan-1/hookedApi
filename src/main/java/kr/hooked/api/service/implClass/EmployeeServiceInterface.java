package kr.hooked.api.service.implClass;

import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.EmployeeUpdateRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface EmployeeServiceInterface {
    EmployeeResponseDto insert(EmployeeRequestDto employeeRequestDto);
    PageResponseDto<EmployeeResponseDto> selectAll(PageRequestDto pageRequestDto);
    EmployeeResponseDto updatePassword(PasswordRequestDto passwordRequestDto, UserDetails userDetails);
    EmployeeResponseDto select(long employeeId);
    EmployeeResponseDto update(EmployeeUpdateRequestDto employeeUpdateRequestDto);
}
