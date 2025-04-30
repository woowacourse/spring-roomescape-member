//package roomescape.fixture;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import roomescape.domain.ReservationTime;
//import roomescape.dto.ReservationTimeRequest;
//import roomescape.dto.ReservationTimeResponse;
//import roomescape.service.ReservationTimeService;
//
//public class ReservationTimeServiceStub extends ReservationTimeService {
//    private final Map<Long, ReservationTime> times = new HashMap<>();
//    private long id = 1L;
//
//    public ReservationTimeServiceStub() {
//        super(null, null);
//    }
//
//    @Override
//    public ReservationTimeResponse create(ReservationTimeRequest request) {
//        ReservationTime time = new ReservationTime(id, request.startAt());
//        times.put(id++, time);
//        return ReservationTimeResponse.from(time);
//    }
//
//    @Override
//    public List<ReservationTimeResponse> getAll() {
//        return ReservationTimeResponse.from(times.values().stream().toList());
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        times.remove(id);
//    }
//}
