package roomescape.domain;

public record Member(long id, String email, String password, String name, Role role) {
}
