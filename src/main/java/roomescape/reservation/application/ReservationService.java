package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        validateNoDuplicate(request);
        ReservationTime time = findReservationTime(request.timeId());
        Theme theme = findTheme(request.themeId());
        return ReservationResponse.from(reservationRepository.save(ReservationRequest.toEntity(request, time, theme)));
    }

    public void cancelReservation(Long id) {
        validateDeletable(id);
        reservationRepository.deleteById(id);
    }

    private void validateDeletable(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException("존재하지 않는 예약ID 입니다.");
        }
    }

    private void validateNoDuplicate(ReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeAndTheme(
                request.date(), request.timeId(), request.themeId())) {
            throw new DuplicateReservationException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    private ReservationTime findReservationTime(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeNotFoundException("존재하지 않는 시간ID 입니다."));
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));
    }
}
