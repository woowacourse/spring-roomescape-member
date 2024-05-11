package roomescape.domain;

public class MemberFixture {
    public static Member defaultValue() {
        return of("test", "test@email.com", "1234");
    }

    public static Member of(String name, String email, String password) {
        return new Member(new PlayerName(name), email, password, Role.BASIC);
    }
}
