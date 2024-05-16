package roomescape.service.dto;

import roomescape.domain.Member;

public class MemberResponse {

    private final Long id;
    private final String name;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName().getValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
