package kr.hooked.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2
public class PageResponseDto<T> {
    private List<T> content; // 현재 페이지 데이터 리스트
    private int currentPage; // 현재 페이지 번호
    private int totalPage; // 전체 페이지 개수
    private boolean hasPreviousPage; // 이전 페이지 여부
    private int prevPage; // 이전 페이지
    private boolean hasNextPage; // 다음 페이지 여부
    private int nextPage; // 다음 페이지
    private List<Integer> pageNumber; // 화면에 표시할 페이지 번호 목록

    public <E> PageResponseDto(Page<E> page, List<T> contentDtoList, PageRequestDto pageRequestDto) {
        this.content = contentDtoList;
        this.currentPage = pageRequestDto.getPage(); // 실제 보여지는 현재 페이지 번호
        this.totalPage = page.getTotalPages();

        int pageBlockSize = pageRequestDto.getSize();

        // 페이징 블록 계산 (1~10, 11~20 등)
        int startPage = ((currentPage - 1) / pageBlockSize) * pageBlockSize + 1;
        int endPage = (Math.min(startPage + pageBlockSize - 1, totalPage)) + 1;

        this.pageNumber = IntStream.range(startPage, endPage).boxed().toList(); // 실제 보여지는 페이지 번호 리스트

        this.hasNextPage = pageNumber.getLast() < totalPage;
        this.hasPreviousPage = pageBlockSize < currentPage;

        this.prevPage = pageNumber.getFirst() - 1 < 0 ? 1 : pageNumber.getFirst() - 1; // 첫 페이지 -1
        this.nextPage = pageNumber.getLast() + 1; // 마지막 페이지 + 1
    }

}
