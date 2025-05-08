package roomescape.domain;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;

    public Member(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

//    public static Member createMemberWithoutId() {
//
//    }

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
