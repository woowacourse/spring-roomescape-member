package roomescape.domain;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String name, String email) {
        this(id, name, email, null);
    }

    public static Member generateWithPrimaryKey(Member member, Long newPrimaryKey) {
        return new Member(newPrimaryKey, member.name, member.email, member.password);
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
}
