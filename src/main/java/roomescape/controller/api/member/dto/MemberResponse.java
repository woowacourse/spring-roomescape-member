package roomescape.controller.api.member.dto;

import java.util.List;
import roomescape.model.Member;
import roomescape.model.Role;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String password,
        String role
) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.id(), member.name(), member.email(), member.password(), member.role().toString());
    }

    public static List<MemberResponse> from(final List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public Member toEntity() {
        return new Member(this.id, this.name, this.email, this.password, Role.findByName(this.role));
    }
}
