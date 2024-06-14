package roomescape.member.domain;

public record Member(long id, String name, String email, boolean isAdmin) {
}
