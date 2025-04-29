package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.reservationtime.ReservationTimeAlreadyExistsException;
import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository timeRepository;

    public ReservationTimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        if (timeRepository.existsByStartAt(request.startAt())) {
            throw new ReservationTimeAlreadyExistsException();
        }

        ReservationTime newReservationTime = new ReservationTime(request.startAt());
        return ReservationTimeResponse.from(timeRepository.add(newReservationTime));
    }

    public List<ReservationTimeResponse> getAll() {
        return ReservationTimeResponse.from(timeRepository.findAll());
    }

    public void deleteById(Long id) {
        int affectedCount = timeRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

    public ReservationTime getById(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeNotFoundException(id));
    }
}
