package roomescape.dto;

import roomescape.model.MemberName;
import roomescape.model.Role;

public record LoginMember(Long id, Role role, MemberName name, String email) {
}
