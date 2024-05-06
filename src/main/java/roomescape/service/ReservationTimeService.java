package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.BadRequestException;
import roomescape.service.dto.request.ReservationAvailabilityTimeRequest;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.response.ReservationAvailabilityTimeResponse;
import roomescape.service.dto.response.ReservationTimeResponse;

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
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toReservationTime();

        validateDuplicatedTime(reservationTime.getStartAt());

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public boolean deleteById(long id) {
        return reservationTimeDao.deleteById(id);
    }

    private void validateDuplicatedTime(LocalTime startAt) {
        boolean exists = reservationTimeDao.existByStartAt(startAt);
        if (exists) {
            throw new BadRequestException("중복된 시간을 생성할 수 없습니다.");
        }
    }

    public List<ReservationAvailabilityTimeResponse> findReservationAvailabilityTimes(
            ReservationAvailabilityTimeRequest timeRequest)
    {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<Reservation> reservations = reservationDao.findByTheme(timeRequest.themeId());

        return reservationTimes.stream()
                .map(reservationTime -> {
                    boolean isBooked = isReservationBooked(reservations, timeRequest.date(), reservationTime);
                    return ReservationAvailabilityTimeResponse.from(reservationTime, isBooked);
                })
                .toList();
    }

    private boolean isReservationBooked(
            List<Reservation> reservations,
            LocalDate date,
            ReservationTime reservationTime)
    {
        return reservations.stream()
                .anyMatch(reservation -> reservation.hasDateTime(date, reservationTime));
    }
}
