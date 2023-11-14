package fido.currency.controller;

import fido.currency.dto.ApiResult;
import fido.currency.dto.LoginDto;
import fido.currency.dto.UserDto;
import fido.currency.service.Impl.UserServiceImpl;
import fido.currency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/add")
    public ApiResult<UserDto> addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }
    @GetMapping("/all")
    public ApiResult<List<UserDto>> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public ApiResult<UserDto> getById(@PathVariable Long id){
        return userService.getById(id);
    }
    @PatchMapping("/update")
    public ApiResult<UserDto> updateUser(@RequestBody UserDto userDto){
        return userService.update(userDto);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResult<Void> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

}
