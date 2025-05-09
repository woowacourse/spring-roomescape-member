package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.request.CreateReservationServiceRequest;
import roomescape.reservation.application.dto.response.ReservationServiceResponse;
import roomescape.reservation.domain.entity.Reservation;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.entity.ReservationTime;
import roomescape.reservation.domain.exception.ReservationException;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationThemeRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.service.ReservationValidator;
import roomescape.reservation.domain.vo.ReservationDetails;
import roomescape.reservation.global.exception.BusinessRuleViolationException;

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
