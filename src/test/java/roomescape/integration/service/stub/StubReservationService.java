package roomescape.integration.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

// 빠른 Stub 구현을 위해 프로덕션 Service 를 상속 받아서 사용했는데,
// 실수로 메서드를 구현하지 않을 경우 부모 (실제 Service) 의 메서드가 호출될 수 있다.
public class StubReservationService extends ReservationService {
    private final Reservation testReservation = new Reservation(
            1L,
            new Member(1L, "히스타", "hista@woowa.jjang", "1q2w3e4r!", Role.MEMBER),
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, LocalTime.of(12, 0)),
            new Theme(1L, "name", "description", "thumbnail"));

    public StubReservationService() {
        super(null, null, null, null);
    }

    @Override
    public void deleteReservation(Long id) {
    }

    @Override
    public List<ReservationResponse> readReservations() {
        return List.of(ReservationResponse.from(testReservation));
    }

    @Override
    public ReservationResponse saveReservation(CreateReservationRequest request) {
        return ReservationResponse.from(testReservation);
    }

    @Override
    public List<ReservationResponse> readAllWithFilter(Map<String, Object> filter) {
        return null;
    }
}
