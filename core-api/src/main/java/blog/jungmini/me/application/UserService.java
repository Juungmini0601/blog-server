package blog.jungmini.me.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.UserRepository;
import blog.jungmini.me.dto.request.UpdateUserRequest;

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
    public UserEntity register(UserEntity user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, String.format("%s는 이미 가입된 이메일 입니다", user.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserById(Long userId) {
        return userRepository.findByIdOrElseThrow(userId);
    }

    @Transactional
    public UserEntity update(Long userId, UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findByIdOrElseThrow(userId);
        userEntity.setNickname(request.getNickname());
        userEntity.setProfileImageUrl(request.getProfileImageUrl());
        userEntity.setGithubUrl(request.getGithubUrl());
        userEntity.setIntroduction(request.getIntroduction());

        return userRepository.save(userEntity);
    }

    @Transactional
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }
}
