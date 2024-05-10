package roomescape.dto;

import roomescape.domain.Member;

public record MemberPreviewResponse(Long id, String name) {

    public MemberPreviewResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
