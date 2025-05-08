//package kr.hooked.api.repository;
//
//import jakarta.transaction.Transactional;
//import kr.hooked.api.entity.*;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@SpringBootTest
//@Log4j2
//public class EmployeeAuthorityRepositoryTests {
//    @Autowired
//    private EmployeeAuthorityRepository employeeAuthorityRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Test
//    @Transactional
//    public void insertTest(){
//        Employee employee = employeeRepository.findById(10L).orElseThrow();
//        Authority authority = authorityRepository.findById(10L).orElseThrow();
//
//        EmployeeAuthority employeeAuthority = EmployeeAuthority
//                .builder()
//                .employee(employee)
//                .authority(authority)
//                .build();
//
//        EmployeeAuthority result = employeeAuthorityRepository.save(employeeAuthority);
//        log.info(result);
//    }
//
//    @Test
//    public void selectTest(){
//        EmployeeAuthority result = employeeAuthorityRepository.findById(1L).orElseThrow();
//        log.info(result);
//    }
//
//    // 삭제 후 새로 저장 업데이트 x
////    @Test
////    public void updateTest(){
////
////    }
//
//
//    @Test
//    public void deleteTest(){
//        employeeAuthorityRepository.deleteById(1L);
//    }
//
//    @Test
//    public void selectAllTest(){
//        List<EmployeeAuthority> result = employeeAuthorityRepository.findAll();
//        log.info(result);
//    }
//
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void multipleInsertTest(){
//        Employee employee;
//        Authority authority;
//        EmployeeAuthority employeeAuthority;
//        EmployeeAuthority result;
//
//        for(int i = 1; i <= 10; i++){
//            employee = employeeRepository.findById((long) i).orElseThrow();
//            log.info("employee: {}", employee);
//            for(int j = 2; j <= 11; j++){
//                authority = authorityRepository.findById((long) j).orElseThrow();
//                log.info("authority: {}", authority);
//                employeeAuthority = EmployeeAuthority
//                        .builder()
//                        .employee(employee)
//                        .authority(authority)
//                        .build();
//
//                result = employeeAuthorityRepository.save(employeeAuthority);
//                log.info(result);
//            } // end for j
//
//        } // end for i
//
//    }
//
//
//}
