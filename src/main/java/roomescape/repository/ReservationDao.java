package roomescape.repository;

import roomescape.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    Optional<Reservation> findById(Long id);

    Optional<Reservation> findByTimeId(Long timeId);

    boolean isExist(LocalDate date, Long timeId);
}
