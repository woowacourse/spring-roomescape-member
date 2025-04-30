package roomescape.repository;

import java.util.List;
import java.util.Optional;

import roomescape.domain.ReservationTheme;

public interface ReservationThemeRepository {

    ReservationTheme save(ReservationTheme reservationTheme);

    List<ReservationTheme> getAll();

    void remove(ReservationTheme reservation);

    Optional<ReservationTheme> findById(Long id);

    ReservationTheme getById(Long id);

    List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit);
}
