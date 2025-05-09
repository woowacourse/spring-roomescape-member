package roomescape.user.domain.dto;

public record UserResponseDto(Long id, String name, String email, String password) {

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
