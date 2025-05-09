package roomescape.domain.member;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role == null ? Role.getDefaultRole() : role;
    }

    // 패스워드를 null 처리하는 생성자
    public Member(Long id, String name, String email, Role role) {
        this(id, name, email, null, role);
    }

    public static Member generateWithPrimaryKey(Member member, Long newPrimaryKey) {
        return new Member(newPrimaryKey, member.name, member.email, member.password, member.role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
