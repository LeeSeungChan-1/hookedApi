package kr.hooked.api.dto.reponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import kr.hooked.api.entity.Department;
import kr.hooked.api.entity.Employee;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDto {
    private Long id; // 부서ID

    private String number; // 부서번호

    private String name; // 부서명

    private boolean status; // 상태

    public static DepartmentResponseDto toDto(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .number(department.getNumber())
                .name(department.getName())
                .status(department.isStatus())
                .build();
    }

}
