package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedReservationException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.AvailableTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimeDaoAll = reservationTimeDao.findAll();

        return reservationTimeDaoAll.stream()
                .map(ReservationTimeResponse::toDto)
                .toList();
    }

    public ReservationTimeResponse create(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toTime();
        ReservationTime savedReservationTime = reservationTimeDao.create(reservationTime);
        return new ReservationTimeResponse(
                savedReservationTime.getId(),
                savedReservationTime.getStartAt()
        );
    }

    public void delete(Long id) {
        if (reservationTimeDao.findById(id).isEmpty()) {
            throw new ReservationTimeNotFoundException();
        }
        if (reservationDao.findByTimeId(id).isPresent()) {
            throw new ExistedReservationException();
        }
        reservationTimeDao.delete(id);
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeDao.findByDateAndThemeIdWithBooked(date, themeId);
    }
}
