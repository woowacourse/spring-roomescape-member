package roomescape.application.dto;

import java.util.List;
import roomescape.domain.Member;

public record MemberDto(
        Long id,
        String name,
        String email,
        String password
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public static List<MemberDto> from(List<Member> members) {
        return members.stream()
                .map(MemberDto::from)
                .toList();
    }

    public Member toEntity() {
        return Member.of(id, name, email, password);
    }
}
