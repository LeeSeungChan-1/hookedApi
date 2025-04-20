package kr.hooked.api.dto;

import kr.hooked.api.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;

import static kr.hooked.api.util.FileUtil.SAVE_EMPLOYEE_IMAGE_URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {
    private Long employeeId; // 사원id
    private String number; // 사원번호(로그인id)
    private String name; // 이름
    private String email; // 이메일
    private String phoneNumber; // 전화번호
    private LocalDate hireDate; // 입사일자
    private String status; // 상태
    private String departmentName; // 부서명
    private String positionName; // 직책명
    private String contentType; // 이미지 타입
    private String imageUrl; // 이미지주소

    public EmployeeResponseDto(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.number = employee.getNumber();
        this.name = employee.getName();
        this.email = employee.getEmail() == null ? "없음" : employee.getEmail();
        this.phoneNumber = employee.getPhoneNumber() == null ? "없음" : employee.getPhoneNumber();
        this.hireDate = employee.getHireDate() == null ? LocalDate.of(0,0,0) : employee.getHireDate();
        this.status = employee.isStatus() ? "재직중" : "퇴직";
        this.departmentName = employee.getDepartment() == null ? "없음" : employee.getDepartment().getName();
        this.positionName = employee.getPosition() == null ? "없음" : employee.getPosition().getName();
        if(employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
            try {
                // ClassPathResource를 사용하여 이미지 파일 경로를 생성합니다.
                ClassPathResource imgFile = new ClassPathResource("image/employee" + "/" + employee.getImageUrl()); // 스프링에서 제공하는 Resource 추상화 한 구현체 resources 하위부터 폴더명 작성
                Path path = Paths.get(imgFile.getURI());
                byte[] imageBytes = Files.readAllBytes(path);

                // 이미지의 Content-Type 확인 (여기서는 파일 확장자로 처리, 실제로 probeContentType 사용도 가능)
                String contentType = Files.probeContentType(path);
                if(contentType == null) {
                    // 기본적으로 PNG로 설정(상황에 맞게 수정)
                    contentType = "image/png";
                }

                // Base64로 인코딩
                String base64Data = Base64.getEncoder().encodeToString(imageBytes);

                // data URL 형태 생성: data:[contentType];base64,[base64Data]
                String imageDataUrl = "data:" + contentType + ";base64," + base64Data;
                this.imageUrl = imageDataUrl;
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 읽기 실패 시 빈 문자열 또는 기본 이미지 처리
                this.imageUrl = "";
            }
        }else {
            // 이미지가 없는 경우 빈 값 처리
            this.imageUrl = "";
        }
    }

}
