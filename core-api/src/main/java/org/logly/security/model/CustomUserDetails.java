package org.logly.security.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

import com.fasterxml.jackson.annotation.*;

/**
 * JsonTypeInfo 직렬화 및 역직렬화 과정에서 JSON에 타입 정보 포함
 * use: 타입 정보를 추가할 방법, CLASS 는 풀 클래스 이름
 * include: PROPERTY 타입 정보를 객체의 속성으로 포함
 * `@class`에 포함됨
 *
 * JsonIgnoreProperties JSON에 존재하지 않는 속성이 포함되어 있어도 예외를 발생시키지 않음
 * JsonCreator는 역직렬화시 사용할 생성자 지정
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private String password;

    @JsonCreator
    public CustomUserDetails(
            @JsonProperty("userId") Long userId,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public void erasePassword() {
        password = "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
