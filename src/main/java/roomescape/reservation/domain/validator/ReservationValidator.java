package roomescape.reservation.domain.validator;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository repository;

    public void validateDeletable(Long id) {
        if (!repository.existsById(id)) {
            throw new ReservationNotFoundException("존재하지 않는 예약ID 입니다.");
        }
    }

    public void validateNoDuplicate(LocalDate date, Long timeId, Long themeId) {
        if (repository.existsByDateAndTimeAndTheme(date, timeId, themeId)) {
            throw new DuplicateReservationException("이미 해당 시간에 예약이 존재합니다.");
        }
    }
}
