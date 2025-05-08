package kr.hooked.api.service;

import kr.hooked.api.dto.reponse.DepartmentResponseDto;
import kr.hooked.api.dto.reponse.EmployeeResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.DepartmentRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.entity.Department;
import kr.hooked.api.entity.Employee;
import kr.hooked.api.repository.DepartmentRepository;
import kr.hooked.api.service.implClass.DepartmentInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentInterface {
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDto findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서 정보가 존재하지 않습니다."));
        return DepartmentResponseDto.toDto(department);
    }

    @Override
    public PageResponseDto<DepartmentResponseDto> selectAll(PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getRealPage();
        int size = pageRequestDto.getSize();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Department> employeePage = departmentRepository.findAll(pageable);

        List<DepartmentResponseDto> departmentResponseDtoList = employeePage.getContent().stream().map(DepartmentResponseDto::toDto).toList();

        PageResponseDto<DepartmentResponseDto> result = new PageResponseDto<>(employeePage, departmentResponseDtoList, pageRequestDto);

        return result;
    }

    @Override
    public DepartmentResponseDto insert(DepartmentRequestDto departmentRequestDto) {
        if(departmentRepository.existsByNumber(departmentRequestDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서번호가 중복되었습니다.");
        }
        if(departmentRepository.existsByName(departmentRequestDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서명이 중복되었습니다.");
        }

        Department department = Department.of(departmentRequestDto);

        departmentRepository.save(department);
        return DepartmentResponseDto.toDto(department);
    }

    @Override
    public DepartmentResponseDto update(DepartmentRequestDto departmentRequestDto) {
        Department prevDepartment = departmentRepository.findById(departmentRequestDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서 정보가 존재하지 않습니다."));
        if(!prevDepartment.getNumber().equals(departmentRequestDto.getNumber()) && departmentRepository.existsByNumber(departmentRequestDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서번호가 중복되었습니다.");
        }
        if(!prevDepartment.getName().equals(departmentRequestDto.getName()) && departmentRepository.existsByName(departmentRequestDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "부서명이 중복되었습니다.");
        }

        Department department = prevDepartment.setUpdateValue(departmentRequestDto);
        departmentRepository.save(department);
        return DepartmentResponseDto.toDto(department);
    }
}
