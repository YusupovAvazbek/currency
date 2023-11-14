package fido.currency.service.mapper;

import fido.currency.dto.UserDto;
import fido.currency.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Mapping(target = "password",expression = "java(passwordEncoder.encode((userDto.getPassword())))")
    public abstract User toEntity(UserDto userDto);
    public abstract UserDto toDto(User user);
}
