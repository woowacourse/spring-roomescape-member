package roomescape.core.dto.member;

public class MemberResponse {
    private String name;

    public MemberResponse() {
    }

    public MemberResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
