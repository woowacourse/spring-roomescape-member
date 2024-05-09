package roomescape.controller.login;

public record MemberCheckResponse(String name) {

    public static MemberCheckResponse from(LoginMember member) {
        return new MemberCheckResponse(member.name());
    }
}
