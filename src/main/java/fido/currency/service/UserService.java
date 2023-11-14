package fido.currency.service;

import fido.currency.dto.ApiResult;
import fido.currency.dto.UserDto;

import java.util.List;

public interface UserService {
    ApiResult<UserDto> addUser(UserDto userDto);
    ApiResult<UserDto> getById(Long id);
    ApiResult<UserDto> update(UserDto userDto);
    ApiResult<List<UserDto>> getAllUsers();
    ApiResult<Void> deleteUser(Long id);

}
