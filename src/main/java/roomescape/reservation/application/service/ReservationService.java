package roomescape.reservation.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservation.application.exception.ReservationException;
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

    public ReservationQueryResult save(ReservationCreateCommand request, LocalDateTime currentDateTime) {
        ReservationTimeQueryResult timeQueryResult = timeService.findById(request.timeId());
        validateReservationDateTime(request.date(), timeQueryResult.startAt(), currentDateTime);

        validateDuplicateReservation(request);
        ThemeQueryResult themeQueryResult = themeService.findById(request.themeId());

        Reservation reservation = request.toEntity(themeQueryResult.id(), timeQueryResult.id());
        return ReservationQueryResult.from(reservationRepository.save(reservation), themeQueryResult, timeQueryResult);
    }

    public int delete(Long id) {
        return reservationRepository.delete(id);
    }

    private void validateReservationDateTime(LocalDate date, LocalTime startAt, LocalDateTime currentDateTime) {
        LocalDateTime triedDateTime = LocalDateTime.of(date, startAt);

        if (triedDateTime.isBefore(currentDateTime)) {
            throw new ReservationException("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
        }
    }

    private void validateDuplicateReservation(ReservationCreateCommand request) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(request.date(),
                request.themeId(),
                request.timeId()
        );
        if (existsByDateAndTime) {
            throw new ReservationException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
