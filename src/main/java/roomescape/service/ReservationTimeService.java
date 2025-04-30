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
        final ReservationTime convertedRequest = reservationTimeRequest.converToReservationTime();
        final ReservationTime reservationTime = reservationTimeDao.save(convertedRequest);
        return new ReservationTimeResponse(reservationTime);
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
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<Reservation> reservations = reservationDao.findAllByDateAndThemeId(date, themeId);
        List<AvailableTimeResponse> responses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            boolean alreadyBooked = false;
            for (Reservation reservation : reservations) {
                if (reservation.isSameTime(reservationTime)) {
                    alreadyBooked = true;
                    break;
                }
            }
            AvailableTimeResponse response = new AvailableTimeResponse(reservationTime.getId(),
                    reservationTime.getStartAt(), alreadyBooked);
            responses.add(response);
        }
        return responses;
    }
}
