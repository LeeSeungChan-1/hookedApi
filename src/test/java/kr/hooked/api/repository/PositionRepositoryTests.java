package kr.hooked.api.repository;

import kr.hooked.api.entity.Position;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class PositionRepositoryTests {

    @Autowired
    private PositionRepository positionRepository;

    @Test
    public void insertTest(){
        Position position = Position
                .builder()
                .number("004")
                .name("직책4")
                .status(true)
                .build();

        Position result = positionRepository.save(position);
        log.info(result);
    }

    @Test
    public void selectTest(){
        Position result = positionRepository.findById(1L).orElseThrow();
        log.info(result);
    }

    @Test
    public void updateTest(){
        Position position = positionRepository.findById(1L).orElseThrow();
        log.info(position);

        position.setName("직책수정");
        position.setStatus(false);

        positionRepository.save(position);
        log.info(position);
    }

    @Test
    public void deleteTest(){
        positionRepository.deleteById(1L);
    }

    @Test
    public void selectAllTest(){
        List<Position> result = positionRepository.findAll();
        log.info(result);
    }


    @Test
    public void multipleInsertTest(){
        for(int i = 1; i <= 100; i++){
            Position position = Position
                    .builder()
                    .number(String.format("%3s", i).replace(" ", "0"))
                    .name("직책" + i)
                    .status(true)
                    .build();

            Position result = positionRepository.save(position);
            log.info(result);
        }

    }
}
