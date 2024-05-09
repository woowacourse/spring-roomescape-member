package roomescape.domain;

public class Member {

    private Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }
}
