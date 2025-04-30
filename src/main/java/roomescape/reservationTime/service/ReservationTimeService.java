package roomescape.reservationTime.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.AvailableReservationTimeRequest;
import roomescape.reservationTime.dto.AvailableReservationTimeResponse;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.dao.ThemeDao;

@Service
public class ReservationTimeService {
    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Reservation> reservationDao;
    private final ThemeDao themeDao;

    public ReservationTimeService(Dao<ReservationTime> reservationTimeDao, Dao<Reservation> reservationDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public ReservationTimeResponse add(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime newReservationTime = new ReservationTime(null, reservationTimeRequest.startAt());

        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        boolean isAlreadyExisted = reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.getStartAt().equals(reservationTimeRequest.startAt()));

        if(isAlreadyExisted) {
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간이 존재합니다");
        }

        ReservationTime savedReservationTime = reservationTimeDao.add(newReservationTime);
        return new ReservationTimeResponse(savedReservationTime.getId(), savedReservationTime.getStartAt());
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(reservationTime -> new ReservationTimeResponse(
                        reservationTime.getId(), reservationTime.getStartAt()))
                .toList();
    }

    // TODO 고민해보기 dao vs service
    public void deleteById(Long id) {
        reservationTimeDao.findById(id).orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 id의 시간이 존재하지 않습니다"));

        List<Reservation> reservations = reservationDao.findAll();
        boolean isOccupiedTimeId = reservations.stream()
                        .anyMatch(reservation -> reservation.getTime().getId().equals(id));

        if (isOccupiedTimeId) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다");
        }
    }

    public List<AvailableReservationTimeResponse> findByDateAndTheme(
            AvailableReservationTimeRequest availableReservationTimeRequest) {

        themeDao.findById(availableReservationTimeRequest.themeId()).orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마 아이디입니다"));

        List<ReservationTime> occupiedReservationTimes = reservationDao.findAll().stream()
                .filter(reservation -> reservation.getDate().equals(availableReservationTimeRequest.date())
                        && reservation.getTheme().getId().equals(availableReservationTimeRequest.themeId())
                ).map(reservation -> new ReservationTime(reservation.getTime().getId(), reservation.getTime()
                        .getStartAt()))
                .toList();

        List<AvailableReservationTimeResponse> response = new ArrayList<>();

        occupiedReservationTimes.forEach(
                reservationTime -> response.add(new AvailableReservationTimeResponse(
                        reservationTime.getId(), reservationTime.getStartAt(), true))
        );

        List<ReservationTime> nonOccupiedReservationTimes = reservationTimeDao.findAll(); //

        nonOccupiedReservationTimes.removeAll(occupiedReservationTimes);
        nonOccupiedReservationTimes.forEach(
                reservationTime ->  response.add(new AvailableReservationTimeResponse(
                        reservationTime.getId(), reservationTime.getStartAt(), false))
        );

        return response;
    }
}
