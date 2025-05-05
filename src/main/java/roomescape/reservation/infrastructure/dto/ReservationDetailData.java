package roomescape.reservation.infrastructure.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDetailData(
        long id,
        MemberData member,
        ThemeData theme,
        LocalDate date,
        TimeData time
        ) {

    public record MemberData(long id, String name) {
    }

    public record ThemeData(long id, String name, String description, String thumbnail) {
    }

    public record TimeData(long id, LocalTime startAt){
    }
}
