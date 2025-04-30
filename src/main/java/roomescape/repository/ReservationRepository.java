package roomescape.repository;

import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Reservation findById(long id);

    List<Reservation> findAll();

    boolean existByTimeId(long id);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);

    int deleteById(long id);
}
