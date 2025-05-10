package roomescape.presentation.methodresolver;

import roomescape.domain.Role;

public record AuthInfo(
        Long memberId,
        String name,
        Role role
) {
}
