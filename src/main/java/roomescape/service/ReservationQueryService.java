package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationDao;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationDao reservationDao;

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAllReservations().stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
