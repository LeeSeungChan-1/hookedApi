package kr.hooked.api.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class ValidCheck {

    public static Map<String, String> validCheck(BindingResult bindingResult) { // 입력된 값 검증
        // 우선순위를 지정할 맵 : 낮은 숫자가 높은 우선순위
        Map<String, Integer> priorityMap = new HashMap<>();
        // 만약 NotBlank나 NotEmpty 에러가 발생하면 가장 먼저 표시함.
        priorityMap.put("NotBlank", 1);
        // Size 제약은 다음 순위
        priorityMap.put("Size", 2);
        // 필요하다면 다른 제약 조건의 우선순위도 추가
        priorityMap.put("Pattern", 3);
        priorityMap.put("Email", 4);
        // bindingResult의 FieldError들을 먼저 정렬한 후, 그룹핑 및 메시지 결합
        return bindingResult.getFieldErrors()
                .stream() // 스트림으로 처리
                .sorted(Comparator.comparing(error ->
                        priorityMap.getOrDefault(error.getCode(), 100) // 순위대로 정렬 지정되 순위가 없으면 맨 뒤
                ))
                .collect(
                        Collectors.groupingBy(
                                FieldError::getField, // 필드 이름으로 그룹
                                LinkedHashMap::new, // 순서 보장을 위한 LinkedHashMap 사용
                                Collectors.mapping( // 같은 필드의 에러를 하나로 결합
                                        FieldError::getDefaultMessage,
                                        Collectors.joining(", ")
                                )
                        )
                );
    }
}
