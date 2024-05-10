package roomescape.dto.response;

public record LoginedMemberResponse(MemberResponse memberResponse, String token) {
}
