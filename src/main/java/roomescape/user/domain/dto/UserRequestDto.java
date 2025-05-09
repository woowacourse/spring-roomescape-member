package roomescape.user.domain.dto;

public record UserRequestDto(String name, String email, String password) {

    public User toEntity() {
        return new User(name, email, password);
    }
}
