package roomescape.member.controller.dto;

public record MemberSearchResponse(String name) {

    public static MemberSearchResponse from(String name) {
        return new MemberSearchResponse(name);
    }

}
