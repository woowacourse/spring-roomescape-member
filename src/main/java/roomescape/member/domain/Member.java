package roomescape.member.domain;

public record Member(Long id, String name, String email, String password, Role role) {

    public static Member of(Long id, String name, String email, String password, String roleExpression) {
        return new Member(id, name, email, password, Role.getByExpression(roleExpression));
    }

    public Member {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id가 null이 되어서는 안 됩니다.");
        }
        if (name == null) {
            throw new IllegalArgumentException("[ERROR] name이 null이 되어서는 안 됩니다.");
        }
        if (email == null) {
            throw new IllegalArgumentException("[ERROR] email이 null이 되어서는 안 됩니다.");
        }
        if (password == null) {
            throw new IllegalArgumentException("[ERROR] password가 null이 되어서는 안 됩니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("[ERROR] role이 null이 되어서는 안 됩니다.");
        }
    }
}
