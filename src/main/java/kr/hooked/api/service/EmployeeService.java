package kr.hooked.api.service;

import jakarta.transaction.Transactional;
import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.EmployeeRequestDto;
import kr.hooked.api.dto.request.EmployeeUpdateRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PasswordRequestDto;
import kr.hooked.api.config.CustomSecurityConfig;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public EmployeeResponseDto insert(EmployeeRequestDto employeeRequestDto) {
        // 중복 검사
        if(employeeRepository.existsByNumber(employeeRequestDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사원번호가 중복되었습니다.");
        }
        if(employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다.");
        }
        if(employeeRepository.existsByPhoneNumber(employeeRequestDto.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "전화번호가 중복되었습니다.");
        }
        // 엔티티 조회
        Department department = departmentRepository.findById(employeeRequestDto.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서 정보가 존재하지 않습니다."));

        Position position = positionRepository.findById(employeeRequestDto.getPositionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 정보가 존재하지 않습니다."));

        // 추가 데이터 준비
        String imageUrl = fileUtil.saveEmployeeImage(employeeRequestDto.getEmployeeImage()); // 이미지 파일이 있으면 url 반환 아니면 null
        String encodedPassword = customSecurityConfig.passwordEncoder().encode(employeeRequestDto.getPassword());

        // 엔티티 생성
        Employee employee = Employee.of(employeeRequestDto, department, position, encodedPassword, imageUrl);

        // 엔티티 저장 및 반환
        Employee result = employeeRepository.save(employee);
        return EmployeeResponseDto.toDto(result);
    }

    public PageResponseDto<EmployeeResponseDto> selectAll(PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getRealPage();
        int size = pageRequestDto.getSize();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDto> employeeResponseDtoList = employeePage.getContent().stream().map(EmployeeResponseDto::toDto).toList();

        PageResponseDto<EmployeeResponseDto> result = new PageResponseDto<>(employeePage, employeeResponseDtoList, pageRequestDto);

        return result;
    }

    public EmployeeResponseDto updatePassword(PasswordRequestDto passwordRequestDto, UserDetails userDetails) {
        if(!userDetails.getUsername().equals(passwordRequestDto.getNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디가 서로 같지 않습니다.");
        }
        Employee employee = employeeRepository.findByNumber(passwordRequestDto.getNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사원 정보가 존재하지 않습니다."));

        employee.setUpdatePassword(customSecurityConfig.passwordEncoder().encode(passwordRequestDto.getNewPassword())); // 비밀번호 설정

        Employee result = employeeRepository.save(employee); // 저장

        return EmployeeResponseDto.toDto(result);
    }


    public EmployeeResponseDto select(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사원 정보가 존재하지 않습니다."));
        return EmployeeResponseDto.toDto(employee);
    }

    @Override
    public EmployeeResponseDto update(EmployeeUpdateRequestDto employeeUpdateRequestDto) {
        Employee prevEmployee = employeeRepository.findById(employeeUpdateRequestDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사원 정보가 존재하지 않습니다."));

        if(!prevEmployee.getEmail().equals(employeeUpdateRequestDto.getEmail()) && employeeRepository.existsByEmail(employeeUpdateRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다.");
        }
        if(!prevEmployee.getPhoneNumber().equals(employeeUpdateRequestDto.getPhoneNumber()) && employeeRepository.existsByPhoneNumber(employeeUpdateRequestDto.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "전화번호가 중복되었습니다.");
        }

        Department department = departmentRepository.findById(employeeUpdateRequestDto.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서 정보가 존재하지 않습니다."));

        Position position = positionRepository.findById(employeeUpdateRequestDto.getPositionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 정보가 존재하지 않습니다."));

        // 추가 데이터 준비
        String imageUrl = fileUtil.saveEmployeeImage(employeeUpdateRequestDto.getEmployeeImage()); // 이미지 파일이 있으면 url 반환 아니면 null

        Employee employee = prevEmployee.setUpdateValue(employeeUpdateRequestDto, imageUrl, department, position);

        // 엔티티 저장 및 반환
        Employee result = employeeRepository.save(employee);
        return EmployeeResponseDto.toDto(result);

    }
}
