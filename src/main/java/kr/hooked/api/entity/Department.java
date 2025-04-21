package kr.hooked.api.entity;

import jakarta.persistence.*;
import kr.hooked.api.dto.request.DepartmentRequestDto;
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

    public static Department of(DepartmentRequestDto departmentRequestDto) {
        return Department.builder()
                .number(departmentRequestDto.getNumber())
                .name(departmentRequestDto.getName())
                .status(true)
                .build();
    }

    public Department setUpdateValue(DepartmentRequestDto departmentRequestDto) {
        this.number = departmentRequestDto.getNumber();
        this.name = departmentRequestDto.getName();
        this.status = departmentRequestDto.isStatus();
        return this;
    }
}
