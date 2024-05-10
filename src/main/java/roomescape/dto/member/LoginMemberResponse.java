package roomescape.dto.member;

public record LoginMemberResponse(String name) {

    public static LoginMemberResponse from(String name) {
        return new LoginMemberResponse(name);
    }
}
