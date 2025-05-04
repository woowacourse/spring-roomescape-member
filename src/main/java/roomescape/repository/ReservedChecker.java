package roomescape.repository;

import roomescape.dto.ReservationValuesDto;

public interface ReservedChecker {
    boolean contains(ReservationValuesDto reservationValuesDto);
}
