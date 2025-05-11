package roomescape.member.domain;

public record Member(Long id, String name, String email, String password, Role role) {
    // TODO : 검증 로직 필요

    public static Member of(Long id, String name, String email, String password, String roleExpression) {
        return new Member(id, name, email, password, Role.getByExpression(roleExpression));
    }
}
