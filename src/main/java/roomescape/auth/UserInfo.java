package roomescape.auth;

import roomescape.domain.member.Role;

public record UserInfo(Long id, String username, String name, Role role) {

}
