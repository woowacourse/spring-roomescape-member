package roomescape.time.domain.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.domain.exception.ReservationTimeInUseException;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@Component
@RequiredArgsConstructor
public class ReservationTimeValidator {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public void validateDeletable(Long timeId) {
        if (!reservationTimeRepository.existsById(timeId)) {
            throw new ReservationTimeNotFoundException("존재하지 않는 시간ID 입니다.");
        }
        if (reservationRepository.existsByReservationTime(timeId)) {
            throw new ReservationTimeInUseException("해당 시간에 예약이 존재합니다.");
        }
    }
}
