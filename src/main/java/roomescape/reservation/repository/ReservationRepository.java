package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByNameOrderByDateAndTime(String name);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    // think: theme를 도메인으로 넘길지, themeId로 넘길지
    boolean existsByDateAndTimeAndThemeId(Long dateId, Long timeId, Long themeId);

    boolean existsByNameAndDateAndTime(String name, Long dateId, Long timeId);

    boolean updateStatus(Reservation reservation);

}
