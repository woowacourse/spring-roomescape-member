package roomescape.service;

import java.util.List;
import java.util.Optional;
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

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public ReservationResponse create(ReservationRequest request) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(request.timeId());
        if (reservationTime.isEmpty()) {
            throw new IllegalArgumentException("해당하는 시간이 없습니다");
        }
        Reservation reservation = new Reservation(
            request.name(),
            request.date(),
            reservationTime.get());
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    public boolean deleteReservation(Long id) {
        int deleteCount = reservationDao.deleteById(id);
        return deleteCount != 0;
    }
}
