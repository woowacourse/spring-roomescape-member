package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String encodedPassword;

    public Member(Long id, String name, String email, String encodedPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public Member(String name, String email, String encodedPassword) {
        this(null, name, email, encodedPassword);
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
}
