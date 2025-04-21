package kr.hooked.api.service;

import kr.hooked.api.dto.reponse.PositionResponseDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.request.PositionRequestDto;
import kr.hooked.api.entity.Position;
import kr.hooked.api.repository.PositionRepository;
import kr.hooked.api.service.implClass.PositionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PositionService implements PositionServiceInterface {
    private final PositionRepository positionRepository;

    private final String SELECT_POSITION_ERROR = "해당 직책을 찾을 수 없습니다.";
    private final String INSERT_NUMBER_ERROR = "이미 등록된 직책번호입니다.";
    private final String INSERT_NAME_ERROR = "이미 등록된 직책번호입니다.";

    public PositionResponseDto insert(PositionRequestDto positionRequestDto) {

        Position result = positionRepository.save(Position.toEntity(positionRequestDto));

        return PositionResponseDto.toDto(result);
    }

    public PositionResponseDto select(Long positionId) {
        Position result = positionRepository.findById(positionId).get();

        return PositionResponseDto.toDto(result);
    }

    public PositionResponseDto update(PositionRequestDto positionRequestDto) {
        Position position = positionRepository.findById(positionRequestDto.getPositionId()).get(); // 직책 조회

        position.setUpdateValue(positionRequestDto); // 수정사항 입력

        Position result = positionRepository.save(position); // 수정

        return PositionResponseDto.toDto(result); // 반환
    }

    public void delete(Long positionId) {
        positionRepository.deleteById(positionId);
    }

    public PageResponseDto<PositionResponseDto> selectAll(PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getRealPage();
        int size = pageRequestDto.getSize();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Position> positionPage = positionRepository.findAll(pageable);

        List<PositionResponseDto> positionResponseDtoList = positionPage.getContent().stream().map(PositionResponseDto::toDto).toList();

        PageResponseDto<PositionResponseDto> result = new PageResponseDto<>(positionPage, positionResponseDtoList, pageRequestDto);

        return result;
    }

    public Map<String, String> existsById(Long id) {
        if(!positionRepository.existsById(id)) {
            return Map.of("error", SELECT_POSITION_ERROR);
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> duplicationCheck(PositionRequestDto positionRequestDto) {
        Map<String, String> errorMap = new HashMap<>();
        if(positionRepository.existsByNumber(positionRequestDto.getNumber())){
           errorMap.put("number", INSERT_NUMBER_ERROR);
        }
        if(positionRepository.existsByName(positionRequestDto.getName())){
            errorMap.put("name", INSERT_NAME_ERROR);
        }
        return errorMap;
    }
}
