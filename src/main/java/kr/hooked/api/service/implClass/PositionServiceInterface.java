package kr.hooked.api.service.implClass;

import kr.hooked.api.dto.reponse.PageResponseDto;
import kr.hooked.api.dto.reponse.PositionResponseDto;
import kr.hooked.api.dto.request.PageRequestDto;
import kr.hooked.api.dto.request.PositionRequestDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PositionServiceInterface {
    PositionResponseDto insert(PositionRequestDto positionRequestDto);
    PositionResponseDto select(Long positionId);
    PositionResponseDto update(PositionRequestDto positionRequestDto);
    void delete(Long positionId);
    PageResponseDto<PositionResponseDto> selectAll(PageRequestDto pageRequestDto);
}
