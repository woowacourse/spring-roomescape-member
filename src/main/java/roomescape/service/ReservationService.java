package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRequest;
import roomescape.domain.reservation.ReservationResponse;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.exception.ReservationNotFoundException;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationUpdatingDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationQueryingDao reservationQueryingDao;
    private final ReservationUpdatingDao reservationUpdatingDao;
    private final ReservationTimeQueryingDao reservationTimeQueryingDao;

    public ReservationService(ReservationQueryingDao reservationQueryingDao, ReservationUpdatingDao reservationUpdatingDao, ReservationTimeQueryingDao reservationTimeQueryingDao) {
        this.reservationQueryingDao = reservationQueryingDao;
        this.reservationUpdatingDao = reservationUpdatingDao;
        this.reservationTimeQueryingDao = reservationTimeQueryingDao;
    }

    public List<ReservationResponse> read() {
        List<Reservation> reservations = reservationQueryingDao.findAllReservations();
         return reservations.stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        new ReservationTimeResponse(reservation.getTime().getId(), reservation.getTime().getStartAt())
                ))
                .toList();
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationReq) {
        Long generatedId = reservationUpdatingDao.insert(reservationReq);
        ReservationTime reservationTimeById = reservationTimeQueryingDao.findReservationTimeById(reservationReq.getTimeId());
        return new ReservationResponse(generatedId, reservationReq.getName(), reservationReq.getDate(), new ReservationTimeResponse(reservationTimeById.getId(), reservationTimeById.getStartAt()));
    }

    public void update(ReservationRequest newReservationReq, Long id) {
        reservationUpdatingDao.save(id, newReservationReq);
    }

    public void delete(Long id) {
        int count = reservationUpdatingDao.delete(id);

        if (count == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
