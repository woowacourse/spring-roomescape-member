package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.ReservationTimeDao;

@Service
@RequiredArgsConstructor
public class ReservationTimeQueryService {

    private final ReservationTimeDao reservationTimeDao;

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
