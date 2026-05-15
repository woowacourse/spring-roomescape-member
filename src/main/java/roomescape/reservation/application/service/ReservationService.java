package roomescape.reservation.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.RoomEscapeException;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservation.application.exception.ReservationErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationDetail;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.reservationtime.application.service.ReservationTimeService;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.service.ThemeService;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;
    private final ReservationTimeService timeService;

    @Transactional(readOnly = true)
    public List<ReservationQueryResult> findAll() {
        List<ReservationDetail> result = reservationRepository.findAll();
        return result.stream()
                .map(ReservationQueryResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationQueryResult> findAllByName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(this::toQueryResult)
                .toList();
    }

    public ReservationQueryResult save(ReservationCreateCommand request, LocalDateTime currentDateTime) {
        ReservationTimeQueryResult timeQueryResult = timeService.findById(request.timeId());
        validateReservationDateTime(request.date(), timeQueryResult.startAt(), currentDateTime);

        ThemeQueryResult themeQueryResult = themeService.findById(request.themeId());
        validateDuplicateReservation(request);

        Reservation reservation = request.toEntity(themeQueryResult.id(), timeQueryResult.id());
        return ReservationQueryResult.from(reservationRepository.save(reservation), themeQueryResult, timeQueryResult);
    }

    public int delete(Long id) {
        return reservationRepository.delete(id);
    }

    private void validateReservationDateTime(LocalDate date, LocalTime startAt, LocalDateTime currentDateTime) {
        LocalDateTime triedDateTime = LocalDateTime.of(date, startAt);

        if (triedDateTime.isBefore(currentDateTime)) {
            throw new RoomEscapeException(ReservationErrorCode.PAST_RESERVATION_TIME);
        }
    }

    private void validateDuplicateReservation(ReservationCreateCommand request) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(request.date(),
                request.themeId(),
                request.timeId()
        );
        if (existsByDateAndTime) {
            throw new RoomEscapeException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }
    }
}
