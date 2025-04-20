package kr.hooked.api.util;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class SaveFile {
    public static final String SAVE_IMAGE_URL = "./upload/img/"; // 이미지 파일 저장 경로
    public static final String SAVE_EMPLOYEE_IMAGE_URL = "./upload/img/employee/"; // 이미지 파일 저장 경로

    private final long EMPLOYEE_IMAGE_SIZE = 500 * 1024; // 사원사진 크기(kb)
    private final int MIN_WIDTH = 350, MAX_WIDTH = 450, MIN_HEIGHT = 450, MAX_HEIGHT = 550; // 사진 가로 세로 사이즈

    private final List<String> ALLOWED_TYPE_LIST = Arrays.asList("image/jpg", "image/jpeg", "image/png"); // 이미지 파일 확장자

    @PostConstruct
    public void init() { //
        File dir1 = new File(SAVE_IMAGE_URL);
        if (!dir1.exists()) { // 폴더가 없을 경우 생성
            dir1.mkdirs();
        }
        File dir2 = new File(SAVE_EMPLOYEE_IMAGE_URL);
        if (!dir2.exists()) { // 폴더가 없을 경우 생성
            dir2.mkdirs();
        }
    }

    public String saveImage(MultipartFile file, String path) throws RuntimeException {

        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(path + saveFileName);

        try{
            Files.copy(file.getInputStream(), savePath);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return saveFileName;
    }

    public Map<String, String> saveImageCheck(MultipartFile file) {
        Map<String, String> checkResult = new HashMap<>();
        if(file.isEmpty()) {
            return Collections.emptyMap(); // 사진이 없는 경우 패스
        }
        List<String> errorMessageList = new ArrayList<>();

        if(!isAllowedType(file)) { // 파일의 확장자가 맞지 않은 경우
            errorMessageList.add("파일은 " + String.join(", ", ALLOWED_TYPE_LIST) + "확장자만 저장이 가능합니다.");
        }

        if(!isAllowedFileSize(file)){ // 파일의 사이즈가 큰 경우
            errorMessageList.add("파일은 " + EMPLOYEE_IMAGE_SIZE/1000 + "KB까지 저장이 가능합니다.");
        }

        if(!isAllowedImageSize(file)) { // 사진의 가로 세로 사이즈가 맞지 않는 경우
            errorMessageList.add("파일은 가로:" + MIN_WIDTH + "~" + MAX_WIDTH + ", 세로: " + MIN_HEIGHT + "~" + MAX_HEIGHT + "까지 저장이 가능합니다.");
        }

        if(!errorMessageList.isEmpty()) { // 에러 메시지가 있을 경우
            checkResult.put("employeeImage", String.join(", ", errorMessageList));
        }

        return checkResult;
    }

    private boolean isAllowedType(MultipartFile file) { // 파일의 이름을 통해 확장자를 검사
        String mimeType = file.getContentType();
        if (mimeType == null) {
            return false;
        }
        boolean allowed = false; // 타입 검사용
        for (String allowedType : ALLOWED_TYPE_LIST) {
            if (mimeType.equalsIgnoreCase(allowedType)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            return false;
        }
        return true;
    }

    private boolean isAllowedFileSize(MultipartFile file) { // 파일 크기 확인
        if(file.getSize() > EMPLOYEE_IMAGE_SIZE) {
            return false;
        }
        return true;
    }

    private boolean isAllowedImageSize(MultipartFile file) { // 이미지의 가로 세로 사이즈 확인
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return false;
            }
            int width = image.getWidth();
            int height = image.getHeight();

            if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
