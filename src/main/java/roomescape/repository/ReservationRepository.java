package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.repository.dao.ReservationDao;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final ReservationDao reservationDao;

    public List<Reservation> getAll() {
        return reservationDao.selectAll();
    }

    public Reservation save(Reservation reservation) {
        return reservationDao.insertAndGet(reservation);
    }

    public Optional<Reservation> findById(Long reservationId) {
        return reservationDao.selectById(reservationId);
    }

    public Reservation getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
    }

    public void remove(Reservation reservation) {
        reservationDao.deleteById(reservation.id());
    }

    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existDuplicatedDateTime(date, timeId, themeId);
    }
}
