package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ReservationExistException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeCreateResponse create(final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTime reservationTime = reservationTimeDao.create(
                new ReservationTime(reservationTimeCreateRequest.getLocalTime()));
        return new ReservationTimeCreateResponse(reservationTime);
    }

    public void deleteIfNoReservation(final long id) {
        ReservationTime reservationTime = findById(id);
        if (reservationTimeDao.deleteIfNoReservation(reservationTime.getId())) {
            return;
        }
        throw new ReservationExistException("이 시간에 대한 예약이 존재합니다.");
    }

    public ReservationTime findById(final Long id) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);
        if (reservationTime.isEmpty()) {
            throw new NoSuchElementException("예약 시간이 존재하지 않습니다.");
        }
        return reservationTime.get();
    }
}
