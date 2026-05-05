package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.exception.ReservationInUseException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
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
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationCreateCommand command) {
        ReservationTime time = timeRepository.getById(command.timeId());
        Theme theme = themeRepository.getById(command.themeId());
        if (reservationRepository.existsByReservationTimeAndThemeAndDate(time.getId(), theme.getId(), command.date())) {
            throw new ReservationInUseException("이미 예약이 존재합니다.");
        }
        return ReservationResponse.from(reservationRepository.save(command.toEntity(time, theme)));
    }

    public void cancelReservation(Long id) {
        if (reservationRepository.deleteById(id) < 1) {
            throw new ReservationNotFoundException("존재하지 않는 예약ID 입니다.");
        }
    }
}
