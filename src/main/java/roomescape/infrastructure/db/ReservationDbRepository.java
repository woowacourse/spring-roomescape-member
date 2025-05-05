package roomescape.infrastructure.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.entity.Reservation;
import roomescape.domain.repository.ReservationRepository;
import roomescape.global.exception.ResourceNotFoundException;
import roomescape.infrastructure.db.dao.ReservationDao;

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
        reservationDao.deleteById(reservation.id());
    }

    @Override
    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existDuplicatedDateTime(date, timeId, themeId);
    }
}
