package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.validation.ReservationNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.mapper.ReservationMapper;
import roomescape.reservation.repository.dao.ReservationDao;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.dto.UpdateReservationParams;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.reservation.repository.dto.DuplicateReservationCondition;
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
                                reservationTimeDao.getByIdIncludingDeleted(reservation.getTimeId()),
                                themeDao.getByIdIncludingDeleted(reservation.getThemeId()))
                ).toList();
    }

    public Reservation findById(Long id) {
        ReservationEntity reservation = reservationDao.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        return ReservationMapper.toReservation(reservation,
                reservationTimeDao.getByIdIncludingDeleted(reservation.getTimeId()),
                themeDao.getByIdIncludingDeleted(reservation.getThemeId()));
    }

    public List<Reservation> findReservationsFrom(LocalDate localDate) {
        return reservationDao.findAllOnOrAfter(localDate).stream()
                .map(reservation ->
                        ReservationMapper.toReservation(
                                reservation,
                                reservationTimeDao.getByIdIncludingDeleted(reservation.getTimeId()),
                                themeDao.getByIdIncludingDeleted(reservation.getThemeId()))
                ).toList();
    }

    public List<Reservation> findByName(String name) {
        return reservationDao.findByName(name).stream()
                .map(reservation ->
                        ReservationMapper.toReservation(reservation,
                                reservationTimeDao.getByIdIncludingDeleted(reservation.getTimeId()),
                                themeDao.getByIdIncludingDeleted(reservation.getThemeId()))
                ).toList();
    }


    @Transactional
    public Reservation save(CreateReservationParams params) {
        Long id = reservationDao.save(
                params.name(),
                params.date(),
                params.timeId(),
                params.themeId()
        );
        ReservationEntity reservationEntity = new ReservationEntity(
                id,
                params.name(),
                params.date(),
                params.timeId(),
                params.themeId()
        );
        ReservationTimeEntity reservationTimeEntity = reservationTimeDao.getByIdIncludingDeleted(params.timeId());
        ThemeEntity themeEntity = themeDao.getByIdIncludingDeleted(params.themeId());
        return ReservationMapper.toReservation(reservationEntity, reservationTimeEntity, themeEntity);
    }

    /**
     * @param params 업데이트할 Reservation 정보
     * @return 업데이트된 row 개수
     */
    @Transactional
    public int update(UpdateReservationParams params) {
        return reservationDao.update(
                params.reservationId(),
                params.name(),
                params.date(),
                params.timeId(),
                params.themeId());
    }

    /**
     * @param id Reservation 식별자
     * @return 삭제된 row 개수
     */
    @Transactional
    public int deleteById(Long id) {
        return reservationDao.deleteById(id);
    }

    /**
     * @param id Reservation 식별자
     * @return 업데이트된 row 개수
     */
    @Transactional
    public int cancelById(Long id) {
        return reservationDao.updateCancelledById(id, true);
    }

    public boolean existsByDateAndTimeIdAndThemeId(DuplicateReservationCondition condition) {
        return reservationDao.existsValidReservationAt(condition.themeId(), condition.date(), condition.timeId());
    }
}
