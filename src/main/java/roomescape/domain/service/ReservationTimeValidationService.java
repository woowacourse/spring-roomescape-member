package roomescape.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.exception.ReservationException.ReservationTimeInUseException;
import roomescape.domain.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeValidationService {

    private final ReservationRepository reservationRepository;

    public void validateNotInUse(Long reservationTimeId) {
        if (reservationRepository.existsByTimeId(reservationTimeId)) {
            throw new ReservationTimeInUseException("해당 예약 시간을 사용중인 예약이 존재합니다.");
        }
    }
}
