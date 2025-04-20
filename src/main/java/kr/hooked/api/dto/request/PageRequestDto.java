package kr.hooked.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDto {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public int getRealPage() {
        return page - 1;
    }
}
