package roomescape.domain.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.domain.exception.ReservationException.InvalidReservationTimeException;
import roomescape.domain.repository.ReservationRepository;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository reservationRepository;

    public void validateNoDuplication(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existDuplicatedDateTime(date, timeId, themeId)) {
            throw new InvalidReservationTimeException("이미 예약된 시간입니다. 다른 시간을 예약해주세요.");
        }
    }
}
