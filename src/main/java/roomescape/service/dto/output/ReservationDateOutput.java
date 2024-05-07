package roomescape.service.dto.output;

import roomescape.domain.ReservationDate;

public record ReservationDateOutput(String date) {

    public static ReservationDateOutput toOutput(final ReservationDate date) {
        return new ReservationDateOutput(date.asString());
    }
}
