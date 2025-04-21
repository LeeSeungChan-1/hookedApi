package kr.hooked.api.dto.reponse;

import kr.hooked.api.entity.Employee;
import kr.hooked.api.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {
    private Long id; // 사원id
    private String number; // 사원번호(로그인id)
    private String name; // 이름
    private String email; // 이메일
    private String phoneNumber; // 전화번호
    private LocalDate hireDate; // 입사일자
    private boolean status; // 상태
    private DepartmentResponseDto department; // 부서
    private PositionResponseDto position; // 직책
    private String imageUrl; // 이미지주소

    public static EmployeeResponseDto toDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .number(employee.getNumber())
                .name(employee.getName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .hireDate(employee.getHireDate())
                .status(employee.isStatus())
                .department(DepartmentResponseDto.toDto(employee.getDepartment()))
                .position(PositionResponseDto.toDto(employee.getPosition()))
                .imageUrl(FileUtil.imageToBase64(employee.getImageUrl()))
                .build();
    }

}
