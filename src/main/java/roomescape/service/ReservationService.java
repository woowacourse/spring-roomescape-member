package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime);

        validateDateTimeExistence(reservation.getDate(), reservationTime.getId());

        Reservation savedReservation = reservationDao.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public boolean deleteById(long id) {
        return reservationDao.deleteById(id);
    }

    private void validateDateTimeExistence(LocalDate date, Long timeId) {
        boolean exists = reservationDao.existsByDateTime(date, timeId);
        if (exists) {
            throw new IllegalArgumentException("중복된 예약 시간입니다.");
        }
    }
}
