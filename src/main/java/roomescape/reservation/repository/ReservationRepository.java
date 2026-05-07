package roomescape.reservation.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.mapper.ReservationMapper;
import roomescape.reservation.repository.dao.ReservationDao;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.theme.repository.dao.ThemeDao;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.time.repository.dao.ReservationTimeDao;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public List<Reservation> findAll() {
        return reservationDao.selectAll().stream()
                .map(reservation ->
                        ReservationMapper.toReservation(reservation,
                                reservationTimeDao.getById(reservation.getTimeId()),
                                themeDao.getById(reservation.getThemeId()))
                ).toList();
    }

    public Reservation save(CreateReservationParams params) {
        Long id = reservationDao.insert(params.name(), params.date(), params.timeId(), params.themeId());
        ReservationEntity reservationEntity = new ReservationEntity(id, params.name(), params.date(), params.timeId(), params.themeId());
        ReservationTimeEntity reservationTimeEntity = reservationTimeDao.getById(params.timeId());
        ThemeEntity themeEntity = themeDao.getById(params.themeId());
        return ReservationMapper.toReservation(reservationEntity, reservationTimeEntity, themeEntity);
    }

    public void deleteById(Long id) {
        int deletedCount = reservationDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 예약 번호입니다.");
        }
    }
}
