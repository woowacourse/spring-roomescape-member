package roomescape.member.service;

import roomescape.member.domain.Member;

public record MemberResponse(long id, String name, String email, String role) {
    public MemberResponse(Member member){
        this(member.getId(), member.getMemberName(), member.getEmail(), member.getRole());
    }
}
