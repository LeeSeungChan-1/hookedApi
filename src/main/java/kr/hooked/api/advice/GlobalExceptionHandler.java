package kr.hooked.api.advice;

import kr.hooked.api.security.util.CustomJWTException;
import kr.hooked.api.util.ValidCheck;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 컬렉션이나 옵셔널에서 요소를 찾을 수 없을 때
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> noSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }
//    // @valid 검증에 실패 했을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = ValidCheck.validCheck(e.getBindingResult()); // @Valid 를 통한 입력값 검증
        return ResponseEntity.badRequest().body(errors);
    }
    //
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<?> responseStatusException(ResponseStatusException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
//    }
    // 요청 본문을 전송하지 않았을 때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }
    // jwt 토큰에 오류가 있을 때
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<Map<String, String>> customJWTException(CustomJWTException e) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataIntegrityViolationException(DataIntegrityViolationException e) { // 중복된 데이터 저장 시
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

}
