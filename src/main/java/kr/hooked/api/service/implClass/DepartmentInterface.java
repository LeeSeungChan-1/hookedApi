package kr.hooked.api.service.implClass;


import jakarta.validation.Valid;
import kr.hooked.api.dto.reponse.DepartmentResponseDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.DepartmentRequestDto;
import kr.hooked.api.dto.request.PageRequestDto;

public interface DepartmentInterface {
    DepartmentResponseDto findById(Long id);

    PageResponseDto<DepartmentResponseDto> selectAll(PageRequestDto pageRequestDto);

    DepartmentResponseDto insert(@Valid DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto update(@Valid DepartmentRequestDto departmentRequestDto);
}
