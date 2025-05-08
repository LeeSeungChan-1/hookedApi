//package kr.hooked.api.repository;
//
//import kr.hooked.api.entity.Authority;
//import kr.hooked.api.entity.Position;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//@Log4j2
//public class AuthorityRepositoryTests {
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Test
//    public void insertTest(){
//        Authority authority = Authority
//                .builder()
//                .name("insertTest")
//                .build();
//        Authority result = authorityRepository.save(authority);
//        log.info(result);
//    }
//
//    @Test
//    public void selectTest(){
//        Authority result = authorityRepository.findById(1L).orElseThrow();
//        log.info(result);
//    }
//
//    @Test
//    public void updateTest(){
//        Authority authority = authorityRepository.findById(1L).orElseThrow();
//        log.info(authority);
//
//        authority.setName("updateTest");
//        Authority result = authorityRepository.save(authority);
//        log.info(result);
//    }
//
//    @Test
//    public void deleteTest(){
//        authorityRepository.deleteById(1L);
//    }
//
//    @Test
//    public void selectAllTest(){
//        List<Authority> result = authorityRepository.findAll();
//        log.info(result);
//    }
//
//
//    @Test
//    public void multipleInsertTest(){
//        for(int i = 1; i <= 100; i++){
//            Authority authority = Authority
//                    .builder()
//                    .name("insertTest" + i)
//                    .build();
//
//            Authority result = authorityRepository.save(authority);
//            log.info(result);
//        }
//
//    }
//}
