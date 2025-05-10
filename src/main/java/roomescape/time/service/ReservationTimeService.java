package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.entity.ReservationTime;
import roomescape.time.exception.ReservationTimeDuplicateException;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository repository;

    public ReservationTimeService(ReservationTimeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ReservationTimeResponse createReservationTime(ReservationTimeRequest request) {
        if (isAnyMatchTime(request.startAt())) {
            throw new ReservationTimeDuplicateException("중복된 예약 시간이 존재합니다.", request.startAt());
        }

        ReservationTime newReservation = repository.save(request.toEntity());
        return ReservationTimeResponse.from(newReservation);
    }

    public List<ReservationTimeResponse> readReservationTimes() {
        return repository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        repository.deleteById(id);
    }

    private boolean isAnyMatchTime(LocalTime time) {
        return repository.findAll().stream()
                .anyMatch(current -> current.getStartAt().equals(time));
    }
}
