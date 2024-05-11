package roomescape.domain;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String password;

    public Member() {
    }

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(Long id, String name, String email, String password) {
        validate(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validate(String name, String email, String password) {
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
