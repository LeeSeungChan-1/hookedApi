package kr.hooked.api.repository;

import kr.hooked.api.entity.Employee;
import kr.hooked.api.entity.EmployeeAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuthorityRepository extends JpaRepository<EmployeeAuthority, Long> {

    void deleteByEmployeeId(Long id);
}
