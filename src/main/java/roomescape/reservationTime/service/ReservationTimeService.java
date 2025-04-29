package roomescape.reservationTime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Reservation> reservationDao;

    public ReservationTimeService(Dao<ReservationTime> reservationTimeDao, Dao<Reservation> reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
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
        boolean isOccupiedId = reservations.stream()
                        .anyMatch(reservation -> reservation.getTime().getId().equals(id));
        if (isOccupiedId) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다");
        }

        reservationTimeDao.deleteById(id);
    }
}
