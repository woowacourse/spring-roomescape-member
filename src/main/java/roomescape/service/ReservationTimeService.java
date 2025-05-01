package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.AvailableTimeResponse;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeDao.isExistsByTime(reservationTimeRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        final ReservationTime reservationTime = reservationTimeRequest.converToReservationTime();
        final ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return new ReservationTimeResponse(savedReservationTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTimeById(final long id) {
        if (reservationDao.isExistsByTimeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }

    public List<AvailableTimeResponse> findAvailableTimes(final LocalDate date, final long themeId) {
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        final List<Reservation> reservations = reservationDao.findAllByDateAndThemeId(date, themeId);
        final List<AvailableTimeResponse> responses = new ArrayList<>();
        return reservationTimes.stream()
                .map(time -> new AvailableTimeResponse(time.getId(), time.getStartAt(),
                        isAlreadyBooked(time, reservations)))
                .toList();
    }

    private boolean isAlreadyBooked(final ReservationTime reservationTime, final List<Reservation> reservations) {
        for (final Reservation reservation : reservations) {
            if (reservation.isSameTime(reservationTime)) {
                return true;
            }
        }
        return false;
    }
}
