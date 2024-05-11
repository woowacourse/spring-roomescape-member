package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.infrastructure.persistence.ReservationRepository;
import roomescape.infrastructure.persistence.dynamic.ReservationQueryConditions;
import roomescape.service.request.ReservationQueryRequest;
import roomescape.service.response.ReservationResponse;

@Component
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> getReservations(ReservationQueryRequest request) {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .memberId(request.memberId())
                .themeId(request.themeId())
                .period(request.fromDate(), request.endDate())
                .build();

        return reservationRepository.findAllBy(conditions).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
