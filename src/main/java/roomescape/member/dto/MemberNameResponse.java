package roomescape.member.dto;

public record MemberNameResponse(
        String name
) {

    public static MemberNameResponse from(String name) {
        return new MemberNameResponse(name);
    }
}
