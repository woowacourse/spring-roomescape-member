package roomescape.dto.member;

public record MemberResponse(String name) {

    public static MemberResponse from(String name) {
        return new MemberResponse(name);
    }
}
