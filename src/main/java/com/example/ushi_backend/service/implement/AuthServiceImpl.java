package com.example.ushi_backend.service.implement;

import com.example.ushi_backend.dto.request.LoginRequest;
import com.example.ushi_backend.dto.request.RequestRegisterOtpRequest;
import com.example.ushi_backend.dto.request.RefreshTokenRequest;
import com.example.ushi_backend.dto.request.RegisterRequest;
import com.example.ushi_backend.dto.response.ApiResponse;
import com.example.ushi_backend.dto.response.AuthResponse;
import com.example.ushi_backend.entity.RegistrationOtpEntity;
import com.example.ushi_backend.entity.UserEntity;
import com.example.ushi_backend.entity.enums.UserRole;
import com.example.ushi_backend.exception.BadRequestException;
import com.example.ushi_backend.exception.ResourceNotFoundException;
import com.example.ushi_backend.exception.UnauthorizedException;
import com.example.ushi_backend.mapper.UserMapper;
import com.example.ushi_backend.repository.RegistrationOtpRepository;
import com.example.ushi_backend.repository.UserRepository;
import com.example.ushi_backend.security.JwtService;
import com.example.ushi_backend.service.AuthService;
import com.example.ushi_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RegistrationOtpRepository registrationOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Value("${app.registration-otp.expiration-minutes:5}")
    private long otpExpirationMinutes;

    @Value("${app.registration-otp.resend-cooldown-seconds:60}")
    private long otpResendCooldownSeconds;

    @Override
    public ApiResponse requestRegisterOtp(RequestRegisterOtpRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email da ton tai");
        }

        registrationOtpRepository.findTopByEmailOrderByCreatedAtDesc(normalizedEmail)
                .ifPresent(existingOtp -> validateOtpCooldown(existingOtp, normalizedEmail));

        String otpCode = generateOtpCode();
        LocalDateTime now = LocalDateTime.now();

        RegistrationOtpEntity otpEntity = RegistrationOtpEntity.builder()
                .email(normalizedEmail)
                .otpCode(otpCode)
                .createdAt(now)
                .expiresAt(now.plusMinutes(otpExpirationMinutes))
                .used(false)
                .build();

        registrationOtpRepository.save(otpEntity);
        emailService.sendRegisterOtp(normalizedEmail, otpCode);

        return new ApiResponse("Da gui OTP den email");
    }

    @Override
    public ApiResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        validatePassword(request.password());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email da ton tai");
        }

        RegistrationOtpEntity latestOtp = registrationOtpRepository.findTopByEmailOrderByCreatedAtDesc(normalizedEmail)
                .orElseThrow(() -> new BadRequestException("Ban chua yeu cau OTP"));
        validateOtp(latestOtp, request.otp());

        UserEntity user = UserEntity.builder()
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .fullName(null)
                .phone(null)
                .address(null)
                .avatarUrl(null)
                .role(UserRole.USER)
                .build();

        latestOtp.setUsed(true);
        registrationOtpRepository.save(latestOtp);

        userRepository.save(user);
        return new ApiResponse("Dang ky thanh cong");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        validatePassword(request.password());

        UserEntity user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UnauthorizedException("Sai email hoac mat khau"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Sai email hoac mat khau");
        }

        return buildAuthResponse("Dang nhap thanh cong", user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Khong tim thay refresh token");
        }

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new UnauthorizedException("Refresh token khong hop le hoac da het han");
        }

        String email = jwtService.extractEmailFromRefreshToken(refreshToken);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung"));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException("Refresh token khong hop le");
        }

        return buildAuthResponse("Lam moi token thanh cong", user);
    }

    @Override
    public ApiResponse logout(RefreshTokenRequest request) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken != null && !refreshToken.isBlank()) {
            userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
                user.setRefreshToken(null);
                userRepository.save(user);
            });
        }

        return new ApiResponse("Dang xuat thanh cong");
    }

    private AuthResponse buildAuthResponse(String message, UserEntity user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        UserEntity savedUser = userRepository.save(user);

        return new AuthResponse(
                message,
                userMapper.toUserResponse(savedUser),
                accessToken,
                refreshToken
        );
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email khong duoc de trong");
        }
        return email.trim().toLowerCase();
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Mat khau khong duoc de trong");
        }
    }

    private void validateOtp(RegistrationOtpEntity otpEntity, String rawOtp) {
        if (rawOtp == null || rawOtp.isBlank()) {
            throw new BadRequestException("OTP khong duoc de trong");
        }
        if (otpEntity.isUsed()) {
            throw new BadRequestException("OTP da duoc su dung");
        }
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP da het han");
        }
        if (!otpEntity.getOtpCode().equals(rawOtp.trim())) {
            throw new BadRequestException("OTP khong chinh xac");
        }
    }

    private void validateOtpCooldown(RegistrationOtpEntity otpEntity, String email) {
        if (!otpEntity.isUsed() && otpEntity.getCreatedAt().plusSeconds(otpResendCooldownSeconds).isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Vui long cho truoc khi gui lai OTP cho email " + email);
        }
    }

    private String generateOtpCode() {
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(otp);
    }

    private String extractRefreshToken(RefreshTokenRequest request) {
        if (request == null || request.refreshToken() == null) {
            return null;
        }
        return request.refreshToken().trim();
    }
}
