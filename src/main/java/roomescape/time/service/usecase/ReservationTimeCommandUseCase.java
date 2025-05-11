package roomescape.time.service.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.BadRequestException;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.ErrorCode;
import roomescape.reservation.service.usecase.ReservationQueryUseCase;
import roomescape.time.service.converter.ReservationTimeConverter;
import roomescape.time.service.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandUseCase {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;

    public ReservationTime create(final CreateReservationTimeServiceRequest createReservationTimeServiceRequest) {
        if (reservationTimeQueryUseCase.existsByStartAt(createReservationTimeServiceRequest.startAt())) {
            throw new ConflictException("추가하려는 시간이 이미 존재합니다.");
        }
        return reservationTimeRepository.save(
                ReservationTimeConverter.toDomain(createReservationTimeServiceRequest));
    }

    public void delete(final ReservationTimeId id) {
        if (reservationQueryUseCase.existsByTimeId(id)) {
            throw new ConflictException("예약에서 참조 중인 시간은 삭제할 수 없습니다.", ErrorCode.CONFLICT_RESERVATION_TIME);
        }
        reservationTimeRepository.deleteById(id);
    }
}
