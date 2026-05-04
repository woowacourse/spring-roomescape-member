package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.validator.ReservationValidator;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ReservationValidator reservationValidator;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime time = timeRepository.getById(request.timeId());
        Theme theme = themeRepository.getById(request.themeId());
        return ReservationResponse.from(reservationRepository.save(ReservationRequest.toEntity(request, time, theme)));
    }

    public void cancelReservation(Long id) {
        reservationValidator.validateDeletable(id);
        reservationRepository.deleteById(id);
    }
}
