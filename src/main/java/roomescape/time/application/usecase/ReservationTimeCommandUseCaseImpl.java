package roomescape.time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.domain.ReservationTimeRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandUseCaseImpl implements ReservationTimeCommandUseCase {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;

    @Override
    public ReservationTime create(final CreateReservationTimeServiceRequest request) {
        if (reservationTimeQueryUseCase.existsByStartAt(request.startAt())) {
            throw new IllegalStateException("추가하려는 시간이 이미 존재합니다.");
        }
        return reservationTimeRepository.save(
                request.toDomain());
    }

    @Override
    public void delete(final ReservationTimeId id) {
        if (!reservationTimeQueryUseCase.existById(id)) {
            throw new NoSuchElementException("삭제할 시간이 존재하지 않습니다.");
        }
        if (reservationQueryUseCase.existsByTimeId(id)) {
            throw new IllegalStateException("예약에서 참조 중인 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
