package roomescape.domain;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Reservation insertReservation(Reservation reservation);

    void deleteReservationById(long id);

    boolean isReservationExistsByTimeId(long timeId);

    boolean isReservationExistsByThemeId(long themeId);

    boolean isReservationExistsById(long id);

    boolean hasSameReservationForThemeAtDateTime(Reservation reservation);
}
