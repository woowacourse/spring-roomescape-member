package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    List<Reservation> findAllByNameOrderByDateAndTime(String name);

    Optional<Reservation> findById(Long id);

    Long save(Reservation reservation);

    void delete(Long id);

    // think: theme를 도메인으로 넘길지, themeId로 넘길지
    boolean existsByDateAndTimeAndThemeId(LocalDate date, LocalTime time, Long themeId);

    boolean existsByNameAndDateAndTime(String name, LocalDate date, LocalTime time);

    boolean updateStatus(Reservation reservation);
}
