package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
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
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간은 예약 불가합니다.");
        }
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
