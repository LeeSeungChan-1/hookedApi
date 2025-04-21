package kr.hooked.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_employeeAuthority")
@Builder
public class EmployeeAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 권한리스트ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee; // 사원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority; // 권한

}
