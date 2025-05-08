package kr.hooked.api.repository;

import kr.hooked.api.entity.Department;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class DepartmentRepositoryTests {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void insertTest(){
        Department department = Department
                .builder()
                .number("001")
                .name("부서1")
                .status(true)
                .build();

        Department result = departmentRepository.save(department);
        log.info(result);

    }

    @Test
    public void selectTest(){
        Department result = departmentRepository.findById(1L).orElseThrow();
        log.info(result);

    }

//    @Test
//    public void updateTest(){
//        Department department = departmentRepository.findById(1L).orElseThrow();
//        log.info(department);
//
//        department.setName("부서수정");
//        department.setStatus(false);
//
//        Department result = departmentRepository.save(department);
//        log.info(result);
//    }

    @Test
    public void deleteTest(){
        departmentRepository.deleteById(2L);
    }

    @Test
    public void selectAllTest(){
        List<Department> result = departmentRepository.findAll();
        log.info(result);
    }


    @Test
    public void multipleInsertTest(){
        for(int i = 1; i <= 100; i++){
            Department department = Department
                    .builder()
                    .number(String.format("%3s", i).replace(" ", "0"))
                    .name("부서" + i)
                    .status(true)
                    .build();

            Department result = departmentRepository.save(department);
            log.info(result);
        }
    }
}


