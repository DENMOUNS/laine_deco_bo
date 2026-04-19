package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.UserMapper;
import cm.dolers.laine_deco.application.usecase.UserService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.email());

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new UserException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, "Email: " + request.email());
        }

        try {
            var user = userMapper.createUserFromRequest(request);
            var saved = userRepository.save(user);
            log.info("User created successfully: {}", saved.getId());
            return userMapper.toUserResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating user", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Failed to create user", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", userId);
                    return new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UserException(ErrorCode.USER_NOT_FOUND, "Email: " + email);
                });
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, CreateUserRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        user.setName(request.name());
        user.setPhone(request.phone());

        var updated = userRepository.save(user);
        log.info("User updated: {}", userId);
        return userMapper.toUserResponse(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);
    }

    @Override
    @Transactional
    public void addLoyaltyPoints(Long userId, Integer points) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        user.setPoints((user.getPoints() != null ? user.getPoints() : 0) + points);
        userRepository.save(user);
        log.info("Loyalty points added for user {}: +{}", userId, points);
    }
}
