package roomescape.repository;

import roomescape.dto.ReservationValueDto;

public interface ReservedChecker {
    boolean contains(ReservationValueDto reservationValueDto);
}
