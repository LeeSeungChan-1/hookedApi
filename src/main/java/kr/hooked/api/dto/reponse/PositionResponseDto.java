package kr.hooked.api.dto.reponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import kr.hooked.api.dto.request.PositionRequestDto;
import kr.hooked.api.entity.Employee;
import kr.hooked.api.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionResponseDto {
    private Long id; // 직책ID

    private String number; // 직책번호

    private String name; // 직책명

    private boolean status; // 상태

    public static PositionResponseDto toDto(Position position) {
        return PositionResponseDto.builder()
                .id(position.getId())
                .number(position.getNumber())
                .name(position.getName())
                .status(position.isStatus())
                .build();
    }
}
