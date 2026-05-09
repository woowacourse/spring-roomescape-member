package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationResult;
import roomescape.service.exception.PastReservationException;
import roomescape.service.exception.ReservationTimeNotFoundException;

@Service
public class UserReservationService {

    private final AdminReservationService reservationService;
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationService(
            AdminReservationService reservationService,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationService = reservationService;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResult create(ReservationCreateCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(
                        "존재하지 않는 시간입니다: timeId=" + command.timeId()));
        validateNotPast(command.date(), time.getStartAt());
        return reservationService.create(command);
    }

    private void validateNotPast(LocalDate date, LocalTime startAt) {
        LocalDateTime reservationAt = LocalDateTime.of(date, startAt);
        if (reservationAt.isBefore(LocalDateTime.now())) {
            throw new PastReservationException(
                    "과거 시점에는 예약할 수 없습니다: " + reservationAt);
        }
    }
}
