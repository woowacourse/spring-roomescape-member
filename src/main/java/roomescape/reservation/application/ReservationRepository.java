package roomescape.reservation.application;

import java.util.List;

public interface ReservationRepository<T> {
    T insert(T t);
    List<T> findAll();
    void delete(Long id);
}
