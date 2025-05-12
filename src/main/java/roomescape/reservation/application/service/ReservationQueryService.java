package roomescape.reservation.application.service;

import roomescape.reservation.application.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.application.dto.ThemeToBookCountServiceResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.ui.ReservationSearchRequest;
import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

import java.util.List;

public interface ReservationQueryService {

    List<Reservation> getAll();

    List<AvailableReservationTimeServiceResponse> getTimesWithAvailability(AvailableReservationTimeServiceRequest availableReservationTimeServiceRequest);

    List<ThemeToBookCountServiceResponse> getRanking(ReservationDate startDate, ReservationDate endDate, int count);

    List<Reservation> getByParams(ReservationSearchRequest request);

    boolean existsByTimeId(ReservationTimeId timeId);

    boolean existsByParams(ReservationDate date, ReservationTimeId timeId, ThemeId themeId);
}
