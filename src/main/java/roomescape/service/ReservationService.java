package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public ReservationResponse save(ReservationRequest request) {
        ReservationTime time = reservationDao.findTimeById(request.timeId());

        if (time == null) {
            throw new IllegalArgumentException("요청하신 시간 ID가 존재하지 않습니다.");
        }

        Reservation reservation = new Reservation(
                request.name(),
                request.date(),
                time
        );

        Reservation saved = reservationDao.save(reservation);

        return ReservationResponse.from(saved);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
