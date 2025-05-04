package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationValidationService {

    private final ReservationRepository reservationRepository;

    public void validateReservationAvailability(LocalDate date, ReservationTime time, ReservationTheme theme) {
        LocalDateTime requestedDateTime = LocalDateTime.of(date, time.startAt());
        validateFutureTime(requestedDateTime);
        validateNoDuplication(date, time.id(), theme.id());
    }

    private void validateFutureTime(LocalDateTime requestedDateTime) {
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateNoDuplication(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existDuplicatedDateTime(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }
}
