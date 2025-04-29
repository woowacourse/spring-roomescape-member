package roomescape.reservation_time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.domain.ReservationTimeRepository;

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
}
