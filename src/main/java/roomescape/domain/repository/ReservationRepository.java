package roomescape.domain.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, long themeId);

    List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate);
}
