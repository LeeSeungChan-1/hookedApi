package kr.hooked.api.service;

import kr.hooked.api.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final String SELECT_DEPARTMENT_ERROR = "해당 부서를 찾을 수 없습니다.";





    public Map<String, String> existsById(Long id) {
        if(!departmentRepository.existsById(id)) {
            return Map.of("error", SELECT_DEPARTMENT_ERROR);
        }
        return Collections.emptyMap();
    }
}
