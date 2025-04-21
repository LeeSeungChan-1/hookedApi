package kr.hooked.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDto {
    private Long id; // 부서 iD

    @NotBlank(message = "부서번호는 필수항목입니다.")
    @Size(min = 3, max = 3, message = "부서번호는 3자리로 입력하셔야합니다.")
    private String number; // 직책번호

    @NotBlank(message = "부서명는 필수항목입니다.")
    @Size(min = 2, max = 10, message = "부서명은 2~10자리로 입력하셔야합니다.")
    private String name; // 직책명

    private boolean status = true; // 상태

}
