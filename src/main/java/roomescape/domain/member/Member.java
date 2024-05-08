package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String encodedPassword;
    private final Role role;

    public Member(Long id, String name, String email, String encodedPassword, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.role = role;
    }

    public Member(String name, String email, String encodedPassword, Role role) {
        this(null, name, email, encodedPassword, role);
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

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Role getRole() {
        return role;
    }
}
