package roomescape.controller.member.dto;

//TODO valid 신경쓰기
public record LoginMember(
        Long id,
        String name,
        String email,
        String role) {
}
