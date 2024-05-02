package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.dto.AvailableTimeDto;

public interface ReservationQueryRepository {

    List<AvailableTimeDto> findAvailableReservationTimes(LocalDate date, long themeId);
}
