package org.logly.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import org.logly.database.entity.UserEntity;
import org.logly.database.repository.UserRepository;
import org.logly.dto.request.CreateUserRequest;
import org.logly.dto.request.UpdateUserRequest;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.security.model.CustomUserDetails;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity register(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(
                    ErrorType.VALIDATION_ERROR, String.format("%s는 이미 가입된 이메일 입니다", request.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(encodedPassword)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserById(Long userId) {
        return userRepository.findByIdOrElseThrow(userId);
    }

    @Transactional
    public UserEntity update(CustomUserDetails details, UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findByIdOrElseThrow(details.getUserId());
        userEntity.setNickname(request.getNickname());
        userEntity.setProfileImageUrl(request.getProfileImageUrl());
        userEntity.setGithubUrl(request.getGithubUrl());
        userEntity.setIntroduction(request.getIntroduction());

        return userRepository.save(userEntity);
    }

    @Transactional
    public void remove(CustomUserDetails details) {
        userRepository.deleteById(details.getUserId());
    }
}
