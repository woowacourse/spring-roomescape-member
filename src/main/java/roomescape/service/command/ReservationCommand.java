package roomescape.service.command;

import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;

public record ReservationCommand(
        MemberName name,
        ReservationDate date,
        Long timeId,
        Long themeId
) {
}
