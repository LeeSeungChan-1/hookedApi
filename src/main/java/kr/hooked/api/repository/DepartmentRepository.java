package kr.hooked.api.repository;

import kr.hooked.api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNumber(String number);

    boolean existsByName(String name);
}
