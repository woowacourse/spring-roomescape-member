package roomescape.member.dto;

public record LoginCheckResponse(String name) {
    public static LoginCheckResponse from(LoginMember member) {
        return new LoginCheckResponse(member.name());
    }
}
