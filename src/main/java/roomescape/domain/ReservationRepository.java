package roomescape.domain;

import java.util.List;
import roomescape.service.dto.ReservationSpecificRequest;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Reservation insertReservation(Reservation reservation);

    void deleteReservationById(long id);

    boolean hasReservationOfTimeId(long timeId);

    boolean hasReservationOfThemeId(long themeId);

    boolean isExistReservationOf(long id);

    boolean hasSameReservationForThemeAtDateTime(Reservation reservation);

    List<Reservation> findSpecificReservations(ReservationSpecificRequest request);
}
