package roomescape.reservation_time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.reservation_time.application.converter.ReservationTimeConverter;
import roomescape.reservation_time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.domain.ReservationTimeRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandUseCaseImpl implements ReservationTimeCommandUseCase {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;

    @Override
    public ReservationTime create(final CreateReservationTimeServiceRequest createReservationTimeServiceRequest) {
        return reservationTimeRepository.save(
                ReservationTimeConverter.toDomain(createReservationTimeServiceRequest));
    }

    @Override
    public void delete(final ReservationTimeId id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        if (reservationQueryUseCase.existsByTimeId(id)) {
            throw new IllegalStateException("예약에서 참조 중인 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
