package roomescape.member.domain;

public class MemberToken {

    private final String token;

    public MemberToken(String token) {
        this.token = token;
    }

    public String asString() {
        return token;
    }
}

