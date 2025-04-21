package kr.hooked.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employeeList")
@Table(name = "tbl_department")
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 부서ID

    @Column(length = 3, unique = true, nullable = false)
    private String number; // 부서번호

    @Column(length = 10, nullable = false)
    private String name; // 부서명

    @Column(nullable = false)
    private boolean status; // 상태

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)

    @Builder.Default
    private List<Employee> employeeList = new ArrayList<>(); // 사원
}
