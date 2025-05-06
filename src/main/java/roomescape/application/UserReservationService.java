package roomescape.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.CreateReservationServiceRequest;
import roomescape.application.dto.response.ReservationServiceResponse;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.exception.ReservationException;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationThemeRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.service.ReservationValidationService;
import roomescape.domain.vo.ReservationDetails;
import roomescape.global.exception.BusinessRuleViolationException;

@Service
@RequiredArgsConstructor
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final ReservationValidationService reservationValidationService;

    public ReservationServiceResponse create(CreateReservationServiceRequest request) {
        ReservationDetails reservationDetails = createReservationDetails(request);
        try {
            reservationValidationService.validateNoDuplication(request.date(), request.timeId(), request.themeId());
            Reservation reservation = Reservation.createFutureReservation(reservationDetails);
            Reservation savedReservation = reservationRepository.save(reservation);
            return ReservationServiceResponse.from(savedReservation);
        } catch (ReservationException e) {
            throw new BusinessRuleViolationException(e.getMessage(), e);
        }
    }

    private ReservationDetails createReservationDetails(CreateReservationServiceRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.getById(request.timeId());
        ReservationTheme reservationTheme = reservationThemeRepository.getById(request.themeId());
        return request.toReservationDetails(reservationTime, reservationTheme);
    }
}
