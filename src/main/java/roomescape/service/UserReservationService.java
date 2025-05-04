package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationValidationService;
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
    private final ReservationValidationService reservationValidationService;

    public ReservationServiceResponse create(CreateReservationServiceRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.getById(request.timeId());
        ReservationTheme reservationTheme = reservationThemeRepository.getById(request.themeId());

        reservationValidationService.validateReservationAvailability(request.date(), reservationTime, reservationTheme);

        Reservation reservation = request.toReservation(reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationServiceResponse.from(savedReservation);
    }
}
