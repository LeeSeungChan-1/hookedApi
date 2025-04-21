package kr.hooked.api.dto.request;

import jakarta.validation.constraints.*;
import kr.hooked.api.entity.Department;
import kr.hooked.api.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequestDto {
    private Long id; // 사원 id

    @NotBlank(message = "사원번호는 필수항목입니다.")
    @Size(min = 8, max = 8, message = "사원번호는 8자리로 입력하셔야합니다.")
    private String number; // 사원번호

    @NotBlank(message = "이름은 필수항목입니다.")
    @Size(min = 2, max = 10, message = "이름은 2~10자리로 입력하셔야합니다.")
    private String name; // 이름

    @Size(min = 10, max = 50, message = "이메일은 10~50자리로 입력하셔야합니다.")
    @Email(message = "이메일 형식으로 입력하셔야합니다.")
    private String email; // 이메일

    @Size(min = 10, max = 11, message = "전화번호는 10~11자리로 입력하셔야합니다.")
    private String phoneNumber; // 전화번호

    private LocalDate hireDate; // 입사일

    private boolean status = true; // 상태

    @NotNull(message = "부서은 필수항목입니다.")
    private Long departmentId = 1L; // 부서id

    @NotNull(message = "직책은 필수항목입니다.")
    private Long positionId = 1L; // 직책id

    private MultipartFile employeeImage; // 사원사진파일

}
