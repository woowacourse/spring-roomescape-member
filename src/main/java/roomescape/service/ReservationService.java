package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.PopularThemeResponse;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.DuplicateReservation;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.TimeNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(this::assignTime)
                .map(this::assignTheme)
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation assignTime(final Reservation reservation) {
        final ReservationTime time = reservationTimeRepository.findById(reservation.getTime().getId())
                .orElse(reservation.getTime());
        return reservation.assignTime(time);
    }

    private Reservation assignTheme(final Reservation reservation) {
        final Theme theme = themeRepository.findById(reservation.getTheme().getId())
                .orElse(reservation.getTheme());
        return reservation.assignTheme(theme);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        final ReservationTime time = reservationTimeRepository
                .findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));
        final Theme theme = themeRepository
                .findById(reservationRequest.themeId())
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));

        final Reservation parsedReservation = reservationRequest.toDomain(time, theme);
        final boolean isExistsReservation = reservationRepository.existsByDateAndTimeId(time.getId(), parsedReservation.getDate());
        if (isExistsReservation) {
            throw new DuplicateReservation("중복된 시간으로 예약이 불가합니다.");
        }

        final LocalDateTime reservationDateTime = parsedReservation.getDate().atTime(time.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }

    public List<PopularThemeResponse> getPopularThemes(final LocalDate today) {
        final List<Theme> reservations = reservationRepository.findPopularThemes(today);
        return reservations.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
