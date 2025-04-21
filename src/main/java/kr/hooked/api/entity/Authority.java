package kr.hooked.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employeeAuthorityList")
@Table(name = "tbl_authority")
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 권한ID

    @Setter
    @Column(length = 20, nullable = false)
    private String name; // 권한명

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmployeeAuthority> employeeAuthorityList = new ArrayList<>(); // 사원권한리스트
}
// select, insert, update, delete

// 인사: HR
// pnSelect, pnInsert, pnUpdate, pnDelete

// 개발: dv
// dvSelect, dvInsert, dvUpdate, dvDelete