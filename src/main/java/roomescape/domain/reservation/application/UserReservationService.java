package roomescape.domain.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.application.dto.request.CreateReservationServiceRequest;
import roomescape.domain.reservation.application.dto.response.ReservationServiceResponse;
import roomescape.domain.reservation.model.dto.ReservationDetails;
import roomescape.domain.reservation.model.entity.Reservation;
import roomescape.domain.reservation.model.entity.ReservationTheme;
import roomescape.domain.reservation.model.entity.ReservationTime;
import roomescape.domain.reservation.model.exception.ReservationException;
import roomescape.domain.reservation.model.repository.ReservationRepository;
import roomescape.domain.reservation.model.repository.ReservationThemeRepository;
import roomescape.domain.reservation.model.repository.ReservationTimeRepository;
import roomescape.domain.reservation.model.service.ReservationValidator;
import roomescape.global.exception.BusinessRuleViolationException;

@Service
@RequiredArgsConstructor
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final ReservationValidator reservationValidator;

    public ReservationServiceResponse create(CreateReservationServiceRequest request) {
        ReservationDetails reservationDetails = createReservationDetails(request);
        try {
            reservationValidator.validateNoDuplication(request.date(), request.timeId(), request.themeId());
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
