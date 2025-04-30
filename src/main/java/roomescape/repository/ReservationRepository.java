package roomescape.repository;

import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    boolean existByTimeId(Long id);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);
}
