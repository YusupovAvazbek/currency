package fido.currency.service.Impl;

import fido.currency.dto.ApiResult;
import fido.currency.dto.LoginDto;
import fido.currency.dto.UserDto;
import fido.currency.model.User;
import fido.currency.repository.UserRepository;
import fido.currency.security.JwtService;
import fido.currency.service.UserService;
import fido.currency.service.additional.AppStatusCodes;
import fido.currency.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fido.currency.service.additional.AppStatusCodes.*;
import static fido.currency.service.additional.AppStatusMessages.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    public static final String USER_ROLE = "USER";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public ApiResult<UserDto> addUser(UserDto userDto) {
        try {
            Optional<User> byUsername = userRepository.findByUsername(userDto.getUsername());
            if(byUsername.isPresent()){
                return ApiResult.<UserDto>builder()
                        .code(AppStatusCodes.VALIDATION_ERROR_CODE)
                        .message("User with this username " + userDto.getUsername() + " already exists!")
                        .build();
            }
            User user = userMapper.toEntity(userDto);
            user.setRole(USER_ROLE);
            userRepository.save(user);
            return ApiResult.<UserDto>builder()
                    .data(userMapper.toDto(user))
                    .message(OK)
                    .build();
        }catch (Exception e){
            return ApiResult.<UserDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message("Error while saving user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ApiResult<UserDto> getById(Long id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isEmpty()) {
                return ApiResult.<UserDto>builder()
                        .code(NOT_FOUND_ERROR_CODE)
                        .message(NOT_FOUND)
                        .build();
            }
            return ApiResult.<UserDto>builder()
                    .message(OK)
                    .data(userMapper.toDto(byId.get()))
                    .build();
        } catch (Exception e) {
            return ApiResult.<UserDto>builder()
                    .code(1)
                    .message(DATABASE_ERROR + ": " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ApiResult<UserDto> update(UserDto userDto) {
        if (userDto.getId() == null) {
            return ApiResult.<UserDto>builder()
                    .message(NULL_VALUE)
                    .code(VALIDATION_ERROR_CODE)
                    .build();
        }
        try {
            Optional<User> userOptional = userRepository.findById(userDto.getId());
            if (userOptional.isEmpty()) {
                return ApiResult.<UserDto>builder()
                        .message(NOT_FOUND)
                        .code(NOT_FOUND_ERROR_CODE)
                        .data(userDto)
                        .build();
            }
            User updatedUser = userOptional.get();
            if (userDto.getUsername() != null) {
                Optional<User> byUsername = userRepository.findByUsername(userDto.getUsername());
                if (byUsername.isPresent() && !userOptional.get().getUsername().equals(userDto.getUsername())) {
                    return ApiResult.<UserDto>builder()
                            .code(AppStatusCodes.VALIDATION_ERROR_CODE)
                            .message(VALIDATION_ERROR)
                            .build();
                }else {
                    updatedUser.setUsername(userDto.getUsername());
                }
            }
            if(userDto.getPassword() != null){
                updatedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            userRepository.save(updatedUser);

            return ApiResult.<UserDto>builder()
                    .data(userMapper.toDto(updatedUser))
                    .message(OK)
                    .code(OK_CODE)
                    .build();
        } catch (Exception e) {
            return ApiResult.<UserDto>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message("Unexpected error with database: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ApiResult<List<UserDto>> getAllUsers() {
        try {
            return ApiResult.<List<UserDto>>builder()
                    .code(OK_CODE)
                    .message(OK)
                    .data(userRepository.findAll().stream()
                            .map(userMapper::toDto)
                            .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            return ApiResult.<List<UserDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message("Unexpected error with database: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ApiResult<Void> deleteUser(Long id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                userRepository.deleteById(id);
                return ApiResult.<Void>builder()
                        .message(OK)
                        .code(OK_CODE)
                        .build();
            }
            return ApiResult.<Void>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(NOT_FOUND)
                    .build();
        } catch (Exception e) {
            return ApiResult.<Void>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message(DATABASE_ERROR)
                    .build();
        }
    }

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND + username));
    }
    public ApiResult<String> login(LoginDto loginDto) {
        UserDto users = loadUserByUsername(loginDto.getUsername());
        if(!passwordEncoder.matches(loginDto.getPassword(), users.getPassword())){
            return ApiResult.<String>builder()
                    .message("Password is not correct")
                    .code(VALIDATION_ERROR_CODE)
                    .build();
        }

        return ApiResult.<String>builder()
                .code(OK_CODE)
                .message(OK)
                .data(jwtService.generateToken(users))
                .build();
    }
}
