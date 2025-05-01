package roomescape.time.service.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationTimeQueryUseCaseImpl implements ReservationTimeQueryUseCase {

    private final ReservationTimeRepository reservationTimeRepository;

    @Override
    public ReservationTime get(final ReservationTimeId id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ReservationTime> getAll() {
        return reservationTimeRepository.findAll();
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        return reservationTimeRepository.existsByStartAt(startAt);
    }

    @Override
    public boolean existById(final ReservationTimeId id) {
        return reservationTimeRepository.existsById(id);
    }

}
