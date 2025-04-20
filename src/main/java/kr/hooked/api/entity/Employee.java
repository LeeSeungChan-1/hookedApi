package kr.hooked.api.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_employees")
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId")
    private Long employeeId; // 사원ID

    @Column(length = 8)
    private String number; // 사원번호

    @Setter
    @Column(length = 10)
    private String name; // 이름

    @Setter
    @Column(length = 50)
    private String email; // 이메일

    @Setter
    @Column(length = 11)
    private String phoneNumber; // 전화번호

    @Setter
    private LocalDate hireDate; // 입사일

    @Setter
    private boolean status; // 상태

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department; // 부서

    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position; // 직책
}
