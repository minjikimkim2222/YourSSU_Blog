package yourssu.blog.domain.user.converter;


import yourssu.blog.domain.user.controller.dto.UserCreateRequest;
import yourssu.blog.domain.user.controller.dto.UserCreateResponse;
import yourssu.blog.domain.user.jpa.UserEntity;
import yourssu.blog.domain.user.model.User;

public class UserConverter {
    public static UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    public static User toUser(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .username(userEntity.getUsername())
                .build();
    }

    public static User toUser(UserCreateRequest userRequest){
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .username(userRequest.getUsername())
                .build();
    }

    public static UserCreateResponse toResponse(User user){
        return UserCreateResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
