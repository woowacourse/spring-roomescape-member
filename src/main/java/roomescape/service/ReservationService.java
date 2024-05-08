package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.ReservationDuplicatedException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.TimeNotFoundException;

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
        final ReservationTime time = getWithValidateTimeNotFound(reservationRequest.timeId());
        final Theme theme = getWithValidateThemeNotFound(reservationRequest.themeId());

        final Reservation parsedReservation = reservationRequest.toDomain(time, theme);
        final boolean isExistsReservation = reservationRepository.existsByDateAndTimeId(time.getId(), parsedReservation.getDate());
        validateTimeDuplicated(isExistsReservation);

        final LocalDateTime reservationDateTime = parsedReservation.getDate().atTime(time.getStartAt());
        validatePreviousTime(reservationDateTime);

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime getWithValidateTimeNotFound(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));
    }

    private Theme getWithValidateThemeNotFound(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));
    }

    private void validateTimeDuplicated(final boolean isExistsReservation) {
        if (isExistsReservation) {
            throw new ReservationDuplicatedException("중복된 시간으로 예약이 불가합니다.");
        }
    }

    private void validatePreviousTime(final LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.delete(id);
    }
}
