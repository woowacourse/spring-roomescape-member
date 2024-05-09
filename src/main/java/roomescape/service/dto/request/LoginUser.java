package roomescape.service.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginUser (Long id, String name, Role role){

    public static LoginUser from(Member member) {
        return new LoginUser(member.getId(), member.getName(), member.getRole());
    }
}
