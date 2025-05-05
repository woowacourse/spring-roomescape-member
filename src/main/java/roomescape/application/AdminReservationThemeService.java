package roomescape.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.CreateReservationThemeServiceRequest;
import roomescape.application.dto.response.ReservationThemeServiceResponse;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.exception.ReservationException;
import roomescape.domain.repository.ReservationThemeRepository;
import roomescape.domain.service.ReservationThemeValidationService;
import roomescape.global.exception.BusinessRuleViolationException;

@Service
@RequiredArgsConstructor
public class AdminReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;
    private final ReservationThemeValidationService reservationThemeValidationService;

    public ReservationThemeServiceResponse create(CreateReservationThemeServiceRequest request) {
        ReservationTheme reservationTheme = reservationThemeRepository.save(request.toReservationTheme());
        return ReservationThemeServiceResponse.from(reservationTheme);
    }

    public void delete(Long id) {
        ReservationTheme reservationTheme = reservationThemeRepository.getById(id);
        try {
            reservationThemeValidationService.validateNotInUse(id);
        } catch (ReservationException e) {
            throw new BusinessRuleViolationException(e.getMessage(), e);
        }
        reservationThemeRepository.remove(reservationTheme);
    }
}
