package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.domain.Theme;

@Service
public class ReservationService {
    private final Dao<Reservation> reservationDao;
    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Theme> themeDao;

    public ReservationService(Dao<Reservation> reservationDao, Dao<ReservationTime> reservationTimeDao, Dao<Theme> themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao =  themeDao;
    }

    public ReservationResponse add(ReservationRequest reservationRequest) {
        ReservationTime findReservationTime = getReservationTime(reservationRequest);
        Theme findTheme = getTheme(reservationRequest);

        Reservation newReservation = new Reservation(
                null,
                reservationRequest.name(),
                reservationRequest.date(),
                findReservationTime,
                findTheme
        );

        List<Reservation> reservations = reservationDao.findAll();
        boolean isAlreadyExisted = reservations.stream()
                .anyMatch(
                        reservation -> reservation.getDate().equals(reservationRequest.date())
                                && reservation.getTime().equals(findReservationTime));
        // TODO : 커스텀 예외 도입할 수도
        if(isAlreadyExisted) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }

        Reservation savedReservation = reservationDao.add(newReservation);
        return new ReservationResponse(
                savedReservation.getId(), savedReservation.getName(), savedReservation.getTheme(), savedReservation.getDate(),
                new ReservationTimeResponse(savedReservation.getTime().getId(), savedReservation.getTime().getStartAt()));
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(), reservation.getName(), reservation.getTheme(), reservation.getDate(),
                        new ReservationTimeResponse(reservation.getTime().getId(), reservation.getTime().getStartAt())))
                .toList();
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] id에 해당하는 예약이 존재하지 않습니다"));
        reservationDao.deleteById(id);
    }

    private ReservationTime getReservationTime(ReservationRequest reservationRequest) {
        return reservationTimeDao
                .findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 시간 id가 존재하지 않습니다."));
    }

    private Theme getTheme(ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }
}
