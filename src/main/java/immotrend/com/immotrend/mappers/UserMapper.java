package immotrend.com.immotrend.mappers;

import immotrend.com.immotrend.dtos.fromView.LoginUserDto;
import immotrend.com.immotrend.dtos.toView.UserDto;
import immotrend.com.immotrend.entities.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toEntity(LoginUserDto loginUserDto){
        if(loginUserDto == null){
            return null;
        }
        User user = new User();
        user.setEmail(loginUserDto.getEmail());
        user.setPassword(loginUserDto.getPassword());

        return user;
    }

    public UserDto toDto(User user){
        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();

        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setPhoto(user.getPhoto());

        return userDto;
    }
}