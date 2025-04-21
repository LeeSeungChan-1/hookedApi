package kr.hooked.api.security.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeSecurityDto extends User {

    private String username; // 사번
    private String password; // 비밀번호
    private String employeeName; // 사원이름
    private String department; // 부서
    private String position; // 직책
    private List<String> authorityList = new ArrayList<>(); // 유저권한리스트

    public EmployeeSecurityDto(String username, String password, String employeeName, String department, String positionm, List<String> authorityList) { // 상속받은 User를 위한 super를 포함한 생성자
        super(username, password,authorityList.stream().map(
                authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                .collect(Collectors.toList()));

        this.username = username;
        this.password = password;
        this.employeeName = employeeName;
        this.department = department;
        this.position = positionm;
        this.authorityList = authorityList;
    }

    public Map<String, Object> getClaims(){ // json 데이터 반환용
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("username", this.username);
        dataMap.put("password", this.password);
        dataMap.put("employeeName", this.employeeName);
        dataMap.put("department", this.department);
        dataMap.put("position", this.position);
        dataMap.put("authorityList", this.authorityList);

        return dataMap;
    }
}
