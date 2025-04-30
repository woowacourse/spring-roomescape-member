package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository repository;

    public ReservationTimeService(ReservationTimeRepository repository) {
        this.repository = repository;
    }

    public List<ReservationTimeResponse> readAllReservationTime() {
        return repository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse postReservationTime(ReservationTimeRequest request) {
        ReservationTime newReservation = repository.save(request.toEntity());
        return ReservationTimeResponse.from(newReservation);
    }

    @Transactional
    public void deleteReservationTime(long id) {
        
        repository.deleteById(id);
    }
}
