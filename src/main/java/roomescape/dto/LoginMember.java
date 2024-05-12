package roomescape.dto;

import roomescape.domain.Member;

public record LoginMember(Long id, String name, String email) {
    public static LoginMember from(Member member) {
        return new LoginMember(
                member.getId(),
                member.getName().getValue(),
                member.getEmail().getValue()
        );
    }
}
