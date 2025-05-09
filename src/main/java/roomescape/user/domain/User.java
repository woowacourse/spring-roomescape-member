package roomescape.user.domain;

public record User(Long id, String name, String email, String password) {
    // TODO : 검증 로직 필요
}
