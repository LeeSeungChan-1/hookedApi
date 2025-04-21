package kr.hooked.api.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Component
@Log4j2
public class FileUtil {

    // 1) 프로젝트 실행 경로(user.dir) 바로 아래 uploads/employee 폴더를 기본 업로드 루트로 설정
    private static final Path UPLOAD_ROOT =
            Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().normalize();
    private static final Path EMPLOYEE_DIR = UPLOAD_ROOT.resolve("employee");

    // 2) 유효성 검사용 상수
    private static final long   MAX_IMAGE_SIZE   = 500 * 1024;    // 500KB
    private static final int    MIN_WIDTH        = 350;
    private static final int    MAX_WIDTH        = 450;
    private static final int    MIN_HEIGHT       = 450;
    private static final int    MAX_HEIGHT       = 550;
    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpg", "image/jpeg", "image/png");

    // 3) 빈 초기화 시 폴더 자동 생성
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(EMPLOYEE_DIR);
            log.info("Upload dirs ready: {}", EMPLOYEE_DIR);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패", e);
            throw new RuntimeException("Could not create upload directories", e);
        }
    }

    /** 직원 이미지 저장 → 파일명 리턴 */
    public String saveEmployeeImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다");
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = EMPLOYEE_DIR.resolve(filename);
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Saved image: {}", target);
            return filename;
        } catch (IOException e) {
            log.error("이미지 저장 실패: {}", filename, e);
            throw new RuntimeException("파일 저장에 실패했습니다", e);
        }
    }

    /** 저장된 파일명을 주면 Base64 data URL 반환 */
    public static String imageToBase64(String filename) {
        if (filename == null || filename.isEmpty()) return "";
        Path path = EMPLOYEE_DIR.resolve(filename).normalize();
        if (!Files.exists(path)) {
            log.warn("이미지 파일 없음: {}", path);
            return "";
        }
        try {
            byte[] bytes = Files.readAllBytes(path);
            String contentType = Optional.ofNullable(Files.probeContentType(path))
                    .orElse("image/png");
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:" + contentType + ";base64," + base64;
        } catch (IOException e) {
            log.error("Base64 인코딩 실패: {}", path, e);
            return "";
        }
    }

    /** 간단 유효성 검사 */
    public Map<String,String> validateEmployeeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return Collections.emptyMap();
        List<String> errs = new ArrayList<>();

        String type = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ALLOWED_TYPES.contains(type)) {
            errs.add("허용 확장자: " + String.join(", ", ALLOWED_TYPES));
        }
        if (file.getSize() > MAX_IMAGE_SIZE) {
            errs.add("파일 크기 최대 " + (MAX_IMAGE_SIZE/1024) + "KB");
        }
        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            if (img == null ||
                    img.getWidth()  < MIN_WIDTH  || img.getWidth()  > MAX_WIDTH ||
                    img.getHeight() < MIN_HEIGHT || img.getHeight() > MAX_HEIGHT) {
                errs.add(String.format("가로 %d~%d, 세로 %d~%d 권장",
                        MIN_WIDTH,MAX_WIDTH,MIN_HEIGHT,MAX_HEIGHT));
            }
        } catch (IOException e) {
            errs.add("이미지 읽기 오류");
        }

        return errs.isEmpty()
                ? Collections.emptyMap()
                : Collections.singletonMap("employeeImage", String.join(", ", errs));
    }
}
