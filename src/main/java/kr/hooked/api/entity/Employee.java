package kr.hooked.api.entity;

import jakarta.persistence.*;

import kr.hooked.api.dto.request.EmployeeRequestDto;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employeeAuthorityList")
@Table(name = "tbl_employee")
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사원id

    @Column(length = 8, unique = true, nullable = false)
    private String number; // 사원번호(로그인id)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(length = 10, nullable = false)
    private String name; // 이름

    @Column(length = 50, unique = true)
    private String email; // 이메일

    @Column(length = 11, unique = true)
    private String phoneNumber; // 전화번호

    private LocalDate hireDate; // 고용일자

    @Column(nullable = false)
    private boolean status; // 상태

    private String imageUrl; // 사원사진Url
    // PERSIST 부모 엔티티에 자식을 추가한 후 저장 시 자식 엔티티 자동 저장
    // MERGE
    @OneToMany(mappedBy = "employee")
    @Builder.Default
    private List<EmployeeAuthority> employeeAuthorityList = new ArrayList<>(); // 사원권한리스트

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department; // 부서

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position; // 직책

    public Employee setUpdatePassword(String password) {
        this.password = password;
        return this;
    }

    public static Employee of(EmployeeRequestDto employeeRequestDto, Department department, Position position, String encodedPassword, String imageUrl) {
        return Employee
                .builder()
                .number(employeeRequestDto.getNumber())
                .password(encodedPassword)
                .name(employeeRequestDto.getName())
                .email(employeeRequestDto.getEmail())
                .phoneNumber(employeeRequestDto.getPhoneNumber())
                .hireDate(employeeRequestDto.getHireDate())
                .department(department)
                .position(position)
                .status(employeeRequestDto.isStatus())
                .imageUrl(imageUrl)
                .build();
    }

}
