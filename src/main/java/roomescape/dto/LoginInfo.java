package roomescape.dto;

import roomescape.domain.entity.Role;

public record LoginInfo(Long id, String name, Role role) {

}
