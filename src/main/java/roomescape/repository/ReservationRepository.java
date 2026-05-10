package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existByDateAndTimeId(LocalDate date, Long timeId);

    List<Reservation> findAllByPaging(int page, int size);
}
