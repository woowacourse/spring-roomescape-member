package roomescape.reservation.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationResult;
import roomescape.reservation.application.exception.ReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.reservationtime.application.exception.ReservationTimeException;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository timeRepository;

    public ReservationResult save(ReservationCreateCommand request, LocalDateTime currentDateTime) {
        ThemeResult themeResult = ThemeResult.from(themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeException("존재하지 않는 테마입니다.")));

        ReservationTimeResult timeResult = ReservationTimeResult.from(timeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 시간입니다.")));

        Reservation reservation = request.toEntity(themeResult.id(), timeResult.id());
        reservation.validateNotPast(timeResult.startAt(), currentDateTime);

        validateDuplicateReservation(reservation);

        return ReservationResult.from(reservationRepository.save(reservation), themeResult, timeResult);
    }

    public int delete(Long id) {
        return reservationRepository.delete(id);
    }

    private void validateDuplicateReservation(Reservation reservation) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(
                reservation.getDate(),
                reservation.getThemeId(),
                reservation.getTimeId()
        );

        if (existsByDateAndTime) {
            throw new ReservationException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
