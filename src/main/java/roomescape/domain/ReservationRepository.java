package roomescape.domain;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation create(Reservation reservation);

    void removeById(Long id);

    boolean hasDuplicateReservation(Reservation reservation);

    boolean hasByTimeId(Long id);

    boolean hasByThemeId(Long id);
}
