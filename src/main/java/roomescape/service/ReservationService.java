package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.persistence.ReservationDao;
import roomescape.persistence.ReservationTimeDao;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationTimeDao reservationTImeDao;
    private final ReservationDao reservationDao;

    public ReservationService(ReservationTimeDao reservationTImeDao,
                              ReservationDao reservationDao) {
        this.reservationTImeDao = reservationTImeDao;
        this.reservationDao = reservationDao;
    }

    public Long create(CreateReservationParam createReservationParam) {
        ReservationTime reservationTime = reservationTImeDao.findById(createReservationParam.timeId()).orElseThrow(
                () -> new IllegalArgumentException(
                        createReservationParam.timeId() + "에 해당하는 reservation_time 튜플이 없습니다."));

        return reservationDao.create(
                new Reservation(
                        createReservationParam.name(),
                        createReservationParam.date(),
                        reservationTime));
    }

    public void deleteById(Long reservationId) {
        reservationDao.deleteById(reservationId);
    }

    public List<ReservationResult> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(this::toReservationResult)
                .toList();
    }

    public ReservationResult findById(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(reservationId + "에 해당하는 reservation 튜플이 없습니다."));
        return toReservationResult(reservation);
    }

    private ReservationResult toReservationResult(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResult(
                        reservation.getTime().id(),
                        reservation.getTime().startAt()));
    }
}
