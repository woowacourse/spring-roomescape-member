package roomescape.service.dto.output;

import roomescape.domain.user.Member;

public record TokenLoginOutput(String name) {
    public static TokenLoginOutput toOutput(final Member member){
        return new TokenLoginOutput(member.getName());
    }
}
