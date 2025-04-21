package kr.hooked.api.repository;

import kr.hooked.api.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"employeeAuthorityList", "department", "position"})
    @Query("select distinct e from Employee e, Department d, Position p where e.number = :number")
    Employee selectWithEmployeeAuthority(@Param("number") String number);

    boolean existsByNumber(String number);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Employee findByNumber(String number);
}
