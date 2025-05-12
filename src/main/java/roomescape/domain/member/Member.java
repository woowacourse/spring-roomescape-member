package roomescape.domain.member;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        validate(name, email);
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

    private void validate(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        // TODO: 이메일 정규식 검증 추가
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
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
