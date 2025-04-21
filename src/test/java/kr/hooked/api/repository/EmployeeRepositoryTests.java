package kr.hooked.api.repository;

import jakarta.transaction.Transactional;
import kr.hooked.api.security.config.CustomSecurityConfig;
import kr.hooked.api.entity.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class EmployeeRepositoryTests {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    CustomSecurityConfig customSecurityConfig;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    EmployeeAuthorityRepository employeeAuthorityRepository;

    @Test
    public void insertTest(){
        Department department = departmentRepository.findById(1L).orElseThrow();
        Position position = positionRepository.findById(1L).orElseThrow();

        Employee employee = Employee
                .builder()
                .number("19990315")
                .password(customSecurityConfig.passwordEncoder().encode("19990315"))
//                .password(customSecurityConfig.passwordEncoder().encode(LocalDate.of(2025, 1, 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
                .name("개발")
                .email("개발자@gmail.com")
                .hireDate(LocalDate.of(2025, 1, 1))
                .phoneNumber("01019990315")
                .status(true)
                .department(department)
                .position(position)
                .build();
        Employee result = employeeRepository.save(employee);
        log.info(result);
    }

    @Test
    @Transactional
    public void selectTest(){
        Employee result = employeeRepository.findById(126L).orElseThrow();
        log.info(result.getEmployeeAuthorityList());
    }

    @Test
    public void updateTest(){
        Employee employee = employeeRepository.findById(1L).orElseThrow();
        log.info(employee);

        Department department = departmentRepository.findById(4L).orElseThrow();
        Position position = positionRepository.findById(4L).orElseThrow();

        employee.setPassword(customSecurityConfig.passwordEncoder().encode("수정"));
        employee.setName("이름수정");
        employee.setEmail("update@gmail.com");
        employee.setHireDate(LocalDate.of(2025, 1, 3));
        employee.setPhoneNumber("01022222222");
        employee.setStatus(false);
        employee.setDepartment(department);
        employee.setPosition(position);


        Employee result = employeeRepository.save(employee);
        log.info(result);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void deleteTest(){
        Employee employee = employeeRepository.findById(1L).orElseThrow();
        employeeAuthorityRepository.deleteByEmployeeEmployeeId(employee.getEmployeeId());
        employeeRepository.deleteById(employee.getEmployeeId());
    }

    @Test
    public void selectAllTest(){
        List<Employee> result = employeeRepository.findAll();
        log.info(result);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void multipleInsertTest(){
        Authority authority2 = authorityRepository.findById(2L).orElseThrow();
        Authority authority3 = authorityRepository.findById(3L).orElseThrow();
        Authority authority4 = authorityRepository.findById(4L).orElseThrow();

        for(int i = 300; i <= 310; i++){
//            Department department = departmentRepository.findById((long) i).orElseThrow();
//            Position position = positionRepository.findById((long) i).orElseThrow();

            Employee employee = Employee
                    .builder()
                    .number(LocalDate.of(2023, 1, 1).plusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .password(customSecurityConfig.passwordEncoder().encode(LocalDate.of(2023, 1, 1).plusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
                    .name("이름" + i)
                    .email("employee" + i + "@gmail.com")
                    .hireDate(LocalDate.of(2023, 1, 1).plusDays(i))
                    .phoneNumber("010" + (12345678 + i))
                    .status(true)
//                    .department(department)
//                    .position(position)
                    .build();

            if(i > 3){
                EmployeeAuthority employeeAuthority = new EmployeeAuthority();
                employeeAuthority.setEmployee(employee);
                employeeAuthority.setAuthority(authority2);

                employeeAuthorityRepository.save(employeeAuthority);
            }
            if (i > 5) {
                EmployeeAuthority employeeAuthority = new EmployeeAuthority();
                employeeAuthority.setEmployee(employee);
                employeeAuthority.setAuthority(authority3);

                employeeAuthorityRepository.save(employeeAuthority);
            }
            if (i > 8) {
                EmployeeAuthority employeeAuthority = new EmployeeAuthority();
                employeeAuthority.setEmployee(employee);
                employeeAuthority.setAuthority(authority4);

                employeeAuthorityRepository.save(employeeAuthority);
            }


            log.info(employee.getNumber());
            log.info(employee.getPhoneNumber());
            Employee result = employeeRepository.save(employee);
            log.info(result);
        }

    }

    @Test
    @Transactional
    public void selectWithEmployeeAuthorityListTest(){
        Employee result = employeeRepository.selectWithEmployeeAuthority("20231028");
        List<String> authorityNames = result.getEmployeeAuthorityList().stream() // 권한명만 순서대로 출력
                .map(ea -> ea.getAuthority().getName())
                .collect(Collectors.toList());
        log.info("Authority Names: {}", authorityNames);
    }
}
