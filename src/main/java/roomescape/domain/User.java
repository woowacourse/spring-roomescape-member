package roomescape.domain;

public record User(
        Long id,
        String name,
        String email,
        String password
) {
}
