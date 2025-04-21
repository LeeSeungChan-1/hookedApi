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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PositionService implements PositionServiceInterface {
    private final PositionRepository positionRepository;

    public PositionResponseDto insert(PositionRequestDto positionRequestDto) {
        if(positionRepository.existsByNumber(positionRequestDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 번호가 중복되었습니다.");
        }
        if(positionRepository.existsByName(positionRequestDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 명이 중복되었습니다.");
        }

        Position result = positionRepository.save(Position.of(positionRequestDto));

        return PositionResponseDto.toDto(result);
    }

    public PositionResponseDto select(Long positionId) {
        Position result = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직책 정보가 존재하지 않습니다."));

        return PositionResponseDto.toDto(result);
    }

    public PositionResponseDto update(PositionRequestDto positionRequestDto) {
        if(positionRepository.existsByNumber(positionRequestDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 번호가 중복되었습니다.");
        }
        if(positionRepository.existsByName(positionRequestDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "직책 명이 중복되었습니다.");
        }

        Position position = positionRepository.findById(positionRequestDto.getPositionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직책 정보가 존재하지 않습니다."));

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
}
