package kr.hooked.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.hooked.api.entity.Position;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequestDto {
    private Long positionId; // 직책ID

    @NotBlank(message = "직책번호는 필수항목입니다.")
    @Size(min = 3, max = 3, message = "직책번호는 3자리로 입력하셔야합니다.")
    private String number; // 직책번호

    @NotBlank(message = "직책명는 필수항목입니다.")
    @Size(min = 3, max = 10, message = "직책명은 3~10자리로 입력하셔야합니다.")
    private String name; // 직책명

    private boolean status = true; // 상태

}
