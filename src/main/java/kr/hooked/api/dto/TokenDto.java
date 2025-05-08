package kr.hooked.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    @NotBlank(message = "엑세스 토큰은 필수항목입니다.")
    private String accessToken;

    @NotBlank(message = "리프레쉬 토큰은 필수항목입니다.")
    private String refreshToken;
}
