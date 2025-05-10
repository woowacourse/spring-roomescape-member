package roomescape.member.auth.dto;

public record MemberInfoResponse(
        Long id,
        String name,
        String email
) {
}
