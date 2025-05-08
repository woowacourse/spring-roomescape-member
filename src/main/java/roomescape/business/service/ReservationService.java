package roomescape.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.business.domain.theme.Theme;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
        final Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
        final Reservation reservation = reservationRequest.convertToReservation(reservationTime, theme);
        if (reservationRepository.existsByDateAndTimeAndTheme(
                reservation.getDate(), reservation.getTimeId(), reservation.getThemeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재합니다.");
        }
        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void cancelReservationById(final long id) {
        reservationRepository.deleteById(id);
    }
}
