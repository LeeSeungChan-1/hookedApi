package kr.hooked.api.dto;

import kr.hooked.api.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResponseDto {
    private String newPassword;

    public PasswordResponseDto(Employee employee) {
        this.newPassword = employee.getPassword();
    }
}
