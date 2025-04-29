package roomescape.service;

import java.util.List;
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
    private final ReservationService reservationService;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao,
                                  final ReservationService reservationService) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationService = reservationService;
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

    public void delete(final long id) {
        if (reservationService.countByTimeId(id) != 0) {
            throw new ReservationExistException("이 시간에 대한 예약이 존재합니다.");
        }
        reservationTimeDao.delete(id);
    }
}
