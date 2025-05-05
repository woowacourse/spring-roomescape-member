package roomescape.time.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.domain.DomainTerm;
import roomescape.common.exception.ConstraintConflictException;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.application.usecase.ReservationQueryUseCase;
import roomescape.time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.domain.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationTimeCommandUseCaseImpl implements ReservationTimeCommandUseCase {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;

    @Override
    public ReservationTime create(final CreateReservationTimeServiceRequest request) {
        if (reservationTimeQueryUseCase.existsByStartAt(request.startAt())) {
            throw new DuplicateException(DomainTerm.RESERVATION_TIME, request.startAt());
        }
        return reservationTimeRepository.save(
                request.toDomain());
    }

    @Override
    public void delete(final ReservationTimeId id) {
        if (!reservationTimeQueryUseCase.existById(id)) {
            throw new NotFoundException(DomainTerm.RESERVATION_TIME, id);
        }
        if (reservationQueryUseCase.existsByTimeId(id)) {
            throw new ConstraintConflictException(DomainTerm.RESERVATION_TIME, id);
        }
        reservationTimeRepository.deleteById(id);
    }
}
