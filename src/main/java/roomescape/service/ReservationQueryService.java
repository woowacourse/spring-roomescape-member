package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationDao;

import java.util.List;

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
