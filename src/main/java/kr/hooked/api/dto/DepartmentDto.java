package kr.hooked.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto {
    private Long departmentId; // 부서ID

    private String number; // 부서번호

    private String name; // 부서명

    private boolean status; // 상태

}
