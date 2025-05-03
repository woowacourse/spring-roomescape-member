package roomescape.business.model.repository;

import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(long id);

    List<Reservation> findAll();

    boolean existById(long id);

    boolean existByTimeId(long timeId);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);

    int deleteById(long id);
}
