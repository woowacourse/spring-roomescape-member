package roomescape.time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.time.application.converter.ReservationTimeConverter;
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
    public ReservationTime create(final CreateReservationTimeServiceRequest createReservationTimeServiceRequest) {
        if (reservationTimeQueryUseCase.existsByStartAt(createReservationTimeServiceRequest.startAt())) {
            throw new IllegalStateException("추가하려는 시간이 이미 존재합니다.");
        }
        return reservationTimeRepository.save(
                ReservationTimeConverter.toDomain(createReservationTimeServiceRequest));
    }

    @Override
    public void delete(final ReservationTimeId id) {
        // TODO 쿼리 유즈케이스에서 하면 더 좋을 듯
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        if (reservationQueryUseCase.existsByTimeId(id)) {
            throw new IllegalStateException("예약에서 참조 중인 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
