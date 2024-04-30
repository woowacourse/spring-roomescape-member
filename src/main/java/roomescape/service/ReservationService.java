package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<Reservation> readReservations() {
        return reservationDao.readReservations();
    }

    public Reservation createReservation(ReservationCreateRequest dto) {
        Reservation reservation = dto.createReservation();
        validate(reservation);
        return reservationDao.createReservation(reservation);
    }

    private void validate(Reservation reservation) {
        if (reservation.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약은 현재 시간 이후여야 합니다.");
        }
        timeDao.readTimeById(reservation.getTime().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 시간이 존재하지 않습니다."));

        if (reservationDao.isReservationsByTimeIdAndDate(reservation.getTime().getId(), reservation.getDate())) {
            throw new IllegalArgumentException("해당 시간대 예약은 이미 존재합니다.");
        }
    }

    public void deleteReservation(long id) {
        reservationDao.deleteReservation(id);
    }

}
