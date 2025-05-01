package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationServiceRequest;
import roomescape.service.dto.response.ReservationServiceResponse;

@Service
@RequiredArgsConstructor
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationServiceResponse create(CreateReservationServiceRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.getById(request.timeId());
        LocalDateTime requestedDateTime = LocalDateTime.of(request.date(), reservationTime.startAt());
        validatePastTime(requestedDateTime);
        validateDuplicatedDateTime(request.date(), request.timeId(), request.themeId());

        ReservationTheme reservationTheme = reservationThemeRepository.getById(request.themeId());

        Reservation reservation = request.toReservation(reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationServiceResponse.from(savedReservation);
    }

    private void validatePastTime(LocalDateTime requestedDateTime) {
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existDuplicatedDateTime(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }
}
