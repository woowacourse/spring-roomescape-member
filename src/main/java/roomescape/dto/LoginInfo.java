package roomescape.dto;

import roomescape.domain.Role;

public record LoginInfo(Long id, String name, Role role) {

}
