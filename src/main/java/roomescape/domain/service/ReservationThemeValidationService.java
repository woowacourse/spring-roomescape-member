package roomescape.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.exception.ReservationException.ReservationThemeInUseException;
import roomescape.domain.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationThemeValidationService {

    private final ReservationRepository reservationRepository;

    public void validateNotInUse(Long reservationThemeId) {
        if (reservationRepository.existsByThemeId(reservationThemeId)) {
            throw new ReservationThemeInUseException("해당 테마를 사용중인 예약이 존재합니다.");
        }
    }
}
