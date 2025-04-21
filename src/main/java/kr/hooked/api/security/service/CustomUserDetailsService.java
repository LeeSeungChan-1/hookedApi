package kr.hooked.api.security.service;

import jakarta.transaction.Transactional;
import kr.hooked.api.security.dto.EmployeeSecurityDto;
import kr.hooked.api.entity.Employee;
import kr.hooked.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.selectWithEmployeeAuthority(username); // username(사원번호)로 조회

        if (employee == null) { // null일 경우 예외
            throw new UsernameNotFoundException(username);
        }

        EmployeeSecurityDto employeeSecurityDto = // userDetail.user를 상속받은 dto 생성
                new EmployeeSecurityDto(
                        employee.getNumber(),
                        employee.getPassword(),
                        employee.getName(),
                        employee.getDepartment() == null ? "" : employee.getDepartment().getName(), // 부서가 없으면 ""
                        employee.getPosition() == null ? "" : employee.getPosition().getName(), // 직책이 없으면 ""
                        employee.getEmployeeAuthorityList().stream()// 권한명만 순서대로 출력
                                .map(ea -> ea.getAuthority().getName())
                                .collect(Collectors.toList()));

        return employeeSecurityDto; // dto 반환
    }
}
