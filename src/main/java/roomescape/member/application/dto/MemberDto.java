package roomescape.member.application.dto;

import java.util.List;
import roomescape.member.domain.Member;

public record MemberDto(
        Long id,
        String email,
        String name
) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getName());
    }

    public static List<MemberDto> from(List<Member> members) {
        return members.stream()
                .map(MemberDto::from)
                .toList();
    }
}
