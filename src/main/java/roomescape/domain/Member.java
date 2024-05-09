package roomescape.domain;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(long id, Member member) {
        this(id, member.name, member.email, member.password);
    }

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
