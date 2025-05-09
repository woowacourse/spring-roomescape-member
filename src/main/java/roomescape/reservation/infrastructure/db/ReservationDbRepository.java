package roomescape.reservation.infrastructure.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.entity.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.global.exception.ResourceNotFoundException;
import roomescape.reservation.infrastructure.db.dao.ReservationDao;

@Repository
@RequiredArgsConstructor
public class ReservationDbRepository implements ReservationRepository {

    private final ReservationDao reservationDao;

    @Override
    public List<Reservation> getAll() {
        return reservationDao.selectAll();
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationDao.insertAndGet(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationDao.selectById(reservationId);
    }

    @Override
    public Reservation getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id에 해당하는 예약이 존재하지 않습니다."));
    }

    @Override
    public void remove(Reservation reservation) {
        reservationDao.deleteById(reservation.getId());
    }

    @Override
    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existDuplicatedDateTime(date, timeId, themeId);
    }

    @Override
    public boolean existsByThemeId(Long reservationThemeId) {
        return reservationDao.existsByThemeId(reservationThemeId);
    }

    @Override
    public boolean existsByTimeId(Long reservationTimeId) {
        return reservationDao.existsByTimeId(reservationTimeId);
    }
}
