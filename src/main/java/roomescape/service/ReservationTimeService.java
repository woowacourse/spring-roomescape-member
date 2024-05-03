package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ReservationTimeWithBookStatusRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao,
                                  ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::fromReservationTime)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();

        validateTimeExistence(reservationTime.getStartAt());

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.fromReservationTime(savedReservationTime);
    }

    public boolean deleteById(long id) {
        return reservationTimeDao.deleteById(id);
    }

    public List<ReservationTimeWithBookStatusResponse> findReservationTimesWithBookStatus(
            ReservationTimeWithBookStatusRequest timeRequest) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<Reservation> reservations = reservationDao.findAll();

        return reservationTimes.stream()
                .map(reservationTime -> {
                    boolean isBooked = isReservationTimeBooked(reservations, timeRequest,
                            reservationTime);
                    return ReservationTimeWithBookStatusResponse.fromReservationTime(
                            reservationTime, isBooked);
                })
                .toList();
    }

    private boolean isReservationTimeBooked(
            List<Reservation> reservations,
            ReservationTimeWithBookStatusRequest timeRequest,
            ReservationTime reservationTime) {
        LocalDate reservationDate = LocalDate.parse(timeRequest.date());
        Long themeId = timeRequest.themeId();

        return reservations.stream()
                .anyMatch(reservation -> reservation.hasCharacteristic(
                        reservationDate, reservationTime, themeId));
    }

    private void validateTimeExistence(LocalTime startAt) {
        boolean exists = reservationTimeDao.existByStartAt(startAt);
        if (exists) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }
}
