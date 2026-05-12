package roomescape.reservation.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public List<Reservation> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation ->
                        ReservationMapper.toReservation(reservation,
                                reservationTimeDao.getById(reservation.getTimeId()),
                                themeDao.getById(reservation.getThemeId()))
                ).toList();
    }

    @Transactional
    public Reservation save(CreateReservationParams params) {
        Long id = reservationDao.insert(params.name(), params.date(), params.timeId(), params.themeId());
        ReservationEntity reservationEntity = new ReservationEntity(id, params.name(), params.date(), params.timeId(), params.themeId());
        ReservationTimeEntity reservationTimeEntity = reservationTimeDao.getById(params.timeId());
        ThemeEntity themeEntity = themeDao.getById(params.themeId());
        return ReservationMapper.toReservation(reservationEntity, reservationTimeEntity, themeEntity);
    }

    /**
     * @param id Reservation 식별자
     * @return 삭제된 row 개수
     */
    @Transactional
    public int deleteById(Long id) {
        return reservationDao.deleteById(id);
    }
}
