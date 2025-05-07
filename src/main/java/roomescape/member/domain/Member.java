package roomescape.member.domain;

import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    private Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member create(String name, String email, String password) {
        return new Member(null, name, email, password);
    }

    public static Member load(Long id, String name, String email, String password) {
        return new Member(id, name, email, password);
    }
}
