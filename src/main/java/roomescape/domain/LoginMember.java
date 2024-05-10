package roomescape.domain;

public class LoginMember {
    private static final long NO_ID = 0;

    private final long id;
    private final String name;
    private final String email;
    private final String password;
    private final String role; //Role 객체로 빼기

    public LoginMember(long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public LoginMember(String name, String email, String password, String role) {
        this(NO_ID, name, email, password, role);
    }

    public LoginMember(long id, String name, String email) {
        this(id, name, email, "", "");
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

    public String getRole() {
        return role;
    }
}
