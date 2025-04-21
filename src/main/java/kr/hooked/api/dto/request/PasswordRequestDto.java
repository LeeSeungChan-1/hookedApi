package kr.hooked.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDto {

    @NotBlank(message = "사원번호는 필수항목입니다.")
    @Size(min = 8, max = 8, message = "사원번호는 8자리로 입력하셔야합니다.")
    private String number; // 사번

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 영어, 숫자, 특수문자(@, $, !, %, *, ?, &)를 포함한 8~20자리로 입력하셔야합니다.")
    private String password; // 현재 비밀번호

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 영어, 숫자, 특수문자(@, $, !, %, *, ?, &)를 포함한 8~20자리로 입력하셔야합니다.")
    private String newPassword; // 새 비밀번호

}
