package roomescape.reservation.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.mapper.ReservationMapper;
import roomescape.reservation.repository.dao.ReservationDao;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.time.repository.dao.ReservationTimeDao;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.selectAll().stream()
                .map(reservation ->
                        ReservationMapper.toReservation(reservation,
                                reservationTimeDao.findById(reservation.getTimeId()))
                ).toList();
    }

    public Reservation save(CreateReservationParams params) {
        Long id = reservationDao.insert(params.name(), params.date(), params.timeId());
        ReservationEntity reservationEntity = new ReservationEntity(id, params.name(), params.date(), params.timeId());
        ReservationTimeEntity reservationTimeEntity = reservationTimeDao.findById(params.timeId());
        return ReservationMapper.toReservation(reservationEntity, reservationTimeEntity);
    }

    public void deleteById(Long id) {
        int deletedCount = reservationDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 예약 번호입니다.");
        }
    }
}
