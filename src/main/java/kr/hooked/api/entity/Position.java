package kr.hooked.api.entity;

import jakarta.persistence.*;
import kr.hooked.api.dto.request.PositionRequestDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employeeList")
@Table(name = "tbl_position")
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 직책ID

    @Column(length = 3, unique = true, nullable = false)
    private String number; // 직책번호

    @Column(length = 10, unique = true, nullable = false)
    private String name; // 직책명

    @Column(nullable = false)
    private boolean status; // 상태

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Employee> employeeList = new ArrayList<>(); // 사원

    public static Position toEntity(PositionRequestDto positionRequestDto) {
        return Position.builder()
                .number(positionRequestDto.getNumber())
                .name(positionRequestDto.getName())
                .status(true)
                .build();
    }

    public Position setUpdateValue(PositionRequestDto positionRequestDto) {
        this.number = positionRequestDto.getNumber();
        this.name = positionRequestDto.getName();
        this.status = positionRequestDto.isStatus();
        return this;
    }
}
