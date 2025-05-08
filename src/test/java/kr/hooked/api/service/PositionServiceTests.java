//package kr.hooked.api.service;
//
//import kr.hooked.api.dto.reponse.PositionResponseDto;
//import kr.hooked.api.dto.request.PageRequestDto;
//import kr.hooked.api.dto.reponse.PageResponseDto;
//import kr.hooked.api.dto.request.PositionRequestDto;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Log4j2
//public class PositionServiceTests {
//    @Autowired
//    private PositionService positionService;
//
////    @Test
////    public void insertTest(){
////        PositionRequestDto positionRequestDto = PositionRequestDto
////                .builder()
////                .number("100")
////                .name("직책서비스1")
////                .status(true)
////                .build();
////
////        PositionResponseDto result = positionService.insert(positionRequestDto);
////        log.info(result);
////    }
//
//    @Test
//    public void selectTest(){
//        PositionResponseDto result = positionService.select(2L);
//        log.info(result);
//    }
//
////    @Test
////    public void updateTest(){
////        PositionRequestDto positionRequestDto = PositionRequestDto
////                .builder()
////                .positionId(2L)
////                .name("직책서비스수정")
////                .status(false)
////                .build();
////        PositionResponseDto result = positionService.update(positionRequestDto);
////        log.info(result);
////    }
//
//    @Test
//    public void deleteTest(){
//        positionService.delete(2L);
//    }
//
//    @Test
//    public void selectAllTest(){
//        PageRequestDto pageRequestDto = PageRequestDto
//                .builder()
//                .page(11)
//                .size(10)
//                .build();
//
//        PageResponseDto result = positionService.selectAll(pageRequestDto);
//        log.info(result);
//    }
//}
