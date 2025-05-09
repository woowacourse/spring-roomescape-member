package roomescape.member.domain;

public class Member {

    private final Long id;
    private final MemberName name;
    private final String email;
    private final String password;

    private Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = new MemberName(name);
        this.email = email;
        this.password = password;
    }

    public static Member createWithId(Long id, String name, String email, String password) {
        return new Member(id, name, email, password);
    }

    public static Member createWithoutId(String name, String email, String password) {
        return new Member(null, name, email, password);
    }

    public Member assignId(Long id) {
        return new Member(id, name.getName(), email, password);
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }
}
