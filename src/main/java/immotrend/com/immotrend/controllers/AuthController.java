package immotrend.com.immotrend.controllers;

import immotrend.com.immotrend.dtos.fromView.LoginUserDto;
import immotrend.com.immotrend.dtos.toView.UserDto;
import immotrend.com.immotrend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    Map<String, String> registerAnUser(
            @RequestParam("name") String name,
            @RequestParam("firstname") String firstname,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        return authService.createUser(name, firstname, email, password);
    }

    @PostMapping("/login")
    Map<String, String> loginAnUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return authService.loginUser(loginUserDto);
    }

    @GetMapping("/me")
    UserDto getMe(Authentication authentication) {
        return authService.getMe(authentication.getPrincipal());
    }

    @GetMapping("/test")
    String test(){
        return "test reussi";
    }
}