package roomescape.member.ui.dto;

public record CreateMemberRequest(
        String email,
        String password,
        String name
) {

}
