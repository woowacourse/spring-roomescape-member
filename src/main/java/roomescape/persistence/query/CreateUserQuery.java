package roomescape.persistence.query;

public record CreateUserQuery(String email, String password, String name) {
}
