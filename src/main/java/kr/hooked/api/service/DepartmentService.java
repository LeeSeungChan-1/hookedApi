package kr.hooked.api.service;

import kr.hooked.api.repository.DepartmentRepository;
import kr.hooked.api.service.implClass.DepartmentInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentInterface {
    private final DepartmentRepository departmentRepository;

}
