package roomescape.dto.response;

import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;

public record MemberResponse(Long id, String email, String name) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getEmail().getEmail(), member.getName().getName());
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail().getEmail(), member.getName().getName());
    }

//    public Member toMember() {
//        return new Member(new Name(name), new Email(email));
//    }
}
