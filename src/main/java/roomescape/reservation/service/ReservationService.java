package roomescape.reservation.service;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.exception.ReservationException;
import roomescape.reservation.repository.ReservationDetail;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;
    private final ReservationTimeService timeService;

    public List<ReservationResponse> findAll() {
        List<ReservationDetail> result = reservationRepository.findAll();
        return result.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationCreateRequest request, LocalDateTime currentDateTime) {
        ReservationTimeResponse timeResponse = timeService.findById(request.timeId());
        validateReservationDateTime(request.date(), timeResponse.startAt(), currentDateTime);

        ThemeResponse themeResponse = themeService.findById(request.themeId());
        validateDuplicateReservation(request);

        Reservation reservation = request.toEntity(themeResponse.id(), timeResponse.id());
        return ReservationResponse.from(reservationRepository.save(reservation), themeResponse, timeResponse);
    }

    private void validateReservationDateTime(LocalDate date, LocalTime startAt, LocalDateTime currentDateTime) {
        LocalDateTime triedDateTime = LocalDateTime.of(date, startAt);

        if (triedDateTime.isBefore(currentDateTime)) {
            throw new ReservationException("[ERROR] 현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
        }
    }

    public int delete(Long id) {
        return reservationRepository.delete(id);
    }

    private void validateDuplicateReservation(ReservationCreateRequest request) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(request.date(),
                request.themeId(),
                request.timeId()
        );
        if (existsByDateAndTime) {
            throw new ReservationException("[ERROR] 이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
