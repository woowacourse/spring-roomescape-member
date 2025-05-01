//package roomescape.fixture;
//
//import java.time.LocalTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import roomescape.domain.Reservation;
//import roomescape.domain.ReservationTime;
//import roomescape.dto.reservation.ReservationRequest;
//import roomescape.dto.reservation.ReservationResponse;
//import roomescape.service.reservation.ReservationService;
//
//public class ReservationServiceStub extends ReservationService {
//    private final Map<Long, Reservation> reservations = new HashMap<>();
//    private long id = 1L;
//
//    public ReservationServiceStub() {
//        super(null, null, null);
//    }
//
//    @Override
//    public ReservationResponse create(ReservationRequest request) {
//        ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
//        Reservation reservation = new Reservation(request.name(), request.date(),
//                time.withId(request.timeId()));
//        Reservation saved = reservation.withId(id++);
//        reservations.put(reservation.getId(), saved);
//        return ReservationResponse.from(saved);
//    }
//
//    @Override
//    public List<ReservationResponse> getAll() {
//        return ReservationResponse.from(reservations.values().stream().toList());
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        reservations.remove(id);
//    }
//}
