package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record MemberResponse(Long id, String email, String password, String name, String role) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getName(),
                member.getRole());
    }

}
