package kr.hooked.api.service;

import jakarta.transaction.Transactional;
import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.reponse.PasswordResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import kr.hooked.api.security.config.CustomSecurityConfig;
import kr.hooked.api.entity.Department;
import kr.hooked.api.entity.Employee;
import kr.hooked.api.entity.Position;
import kr.hooked.api.repository.DepartmentRepository;
import kr.hooked.api.repository.EmployeeRepository;
import kr.hooked.api.repository.PositionRepository;
import kr.hooked.api.service.implClass.EmployeeServiceInterface;
import kr.hooked.api.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmployeeService implements EmployeeServiceInterface {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final CustomSecurityConfig customSecurityConfig;
    private final FileUtil fileUtil;

    private final String SELECT_EMPLOYEE_ERROR = "사원을 찾을 수 없습니다.";
    private final String INSERT_NUMBER_ERROR = "이미 등록된 사원번호입니다.";
    private final String INSERT_EMAIL_ERROR = "이미 등록된 이메일입니다.";
    private final String INSERT_PHONENUMBER_ERROR = "이미 등록된 전화번호입니다.";
    private final String PASSWORD_NOT_EQUAL = "비밀번호를 확인해주세요.";

    @Transactional
    public EmployeeResponseDto insert(EmployeeRequestDto employeeRequestDto) {
        Department department = departmentRepository.findById(employeeRequestDto.getDepartmentId()).get();

        Position position = positionRepository.findById(employeeRequestDto.getPositionId()).get();

        String imageUrl = fileUtil.saveEmployeeImage(employeeRequestDto.getEmployeeImage()); // 이미지 파일이 있으면 url 반환 아니면 null

        employeeRequestDto.setPassword(customSecurityConfig.passwordEncoder().encode(employeeRequestDto.getPassword()));
        employeeRequestDto.setDepartment(department);
        employeeRequestDto.setPosition(position);
        employeeRequestDto.setImageUrl(imageUrl);

        Employee result = employeeRepository.save(Employee.toEntity(employeeRequestDto));

        return EmployeeResponseDto.toDto(result);
    }

    public PageResponseDto<EmployeeResponseDto> selectAll(PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getRealPage();
        int size = pageRequestDto.getSize();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDto> EmployeeResponseDtoList = employeePage.getContent().stream().map(EmployeeResponseDto::toDto).toList();

        PageResponseDto<EmployeeResponseDto> result = new PageResponseDto<>(employeePage, EmployeeResponseDtoList, pageRequestDto);

        return result;
    }

    public EmployeeResponseDto updatePassword(PasswordRequestDto passwordRequestDto) {
        Employee employee = employeeRepository.findByNumber(passwordRequestDto.getNumber()); // 사원번호로 가져오기

        employee.setUpdatePassword(customSecurityConfig.passwordEncoder().encode(passwordRequestDto.getNewPassword())); // 비밀번호 설정

        Employee result = employeeRepository.save(employee); // 저장

        return EmployeeResponseDto.toDto(result);
    }


    public EmployeeResponseDto select(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return EmployeeResponseDto.toDto(employee);
    }


    public Map<String, String> duplicationCheck(EmployeeRequestDto employeeRequestDto) { // 저장전 유니크 항목 검사
        // id 제외 유니크 값 사원번호(number), 이메일(email), 전화번호(phoneNumber)
        Map<String, String> errorMap = new HashMap<>();
        if(employeeRepository.existsByNumber(employeeRequestDto.getNumber())){ // 사원번호로 조회 시 값이 있을 경우
            errorMap.put("number", INSERT_NUMBER_ERROR);
        }
        if(employeeRepository.existsByEmail(employeeRequestDto.getEmail())){ // 이메일로 조회 시 값이 있을 경우
            errorMap.put("email", INSERT_EMAIL_ERROR);
        }
        if(employeeRepository.existsByPhoneNumber(employeeRequestDto.getPhoneNumber())){ // 전화번호 조회 시 값이 있을 경우
            errorMap.put("phoneNumber", INSERT_PHONENUMBER_ERROR);
        }

        return errorMap;
    }

    public Map<String, String> passwordCheck(PasswordRequestDto passwordRequestDto) {
        if(!customSecurityConfig.passwordEncoder()
                .matches(
                        passwordRequestDto.getPassword(), // 입력된 비밀번호
                        employeeRepository.findByNumber(passwordRequestDto.getNumber()).getPassword())){ // 사번으로 조회한 비밀번호
            return Map.of("error", PASSWORD_NOT_EQUAL); // 다르면 에러
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> existsById(Long id) {
        if(!employeeRepository.existsById(id)){
            return Map.of("error", SELECT_EMPLOYEE_ERROR);
        }
        return Collections.emptyMap();
    }
}
