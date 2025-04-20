package kr.hooked.api.repository;

import kr.hooked.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employee, Long> {
}
