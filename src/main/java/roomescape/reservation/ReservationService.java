package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.reason.reservation.ReservationConflictException;
import roomescape.exception.custom.reason.reservation.ReservationNotExistsThemeException;
import roomescape.exception.custom.reason.reservation.ReservationNotExistsTimeException;
import roomescape.exception.custom.reason.reservation.ReservationNotFoundException;
import roomescape.exception.custom.reason.reservation.ReservationPastDateException;
import roomescape.exception.custom.reason.reservation.ReservationPastTimeException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(final ReservationRequest request) {
        validateDuplicateDateTime(request.timeId(), request.date());
        validatePastDateTime(request);
        validateExistsReservationTime(request);
        validateExistsTheme(request);

        final Reservation reservation = new Reservation(request.name(), request.date());
        final long id = reservationRepository.save(reservation, request.timeId(), request.themeId());
        final Reservation savedReservation = reservationRepository.findById(id);
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> readAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.delete(id);
    }

    private void validatePastDateTime(final ReservationRequest request) {
        final LocalDate today = LocalDate.now();
        final LocalDate reservationDate = request.date();
        if (reservationDate.isBefore(today)) {
            throw new ReservationPastDateException();
        }
        if (reservationDate.isEqual(today)) {
            validatePastTime(request);
        }
    }

    private void validatePastTime(final ReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        if (reservationTime.isBefore(LocalTime.now())) {
            throw new ReservationPastTimeException();
        }
    }

    private void validateDuplicateDateTime(final Long reservationTimeId, final LocalDate date) {
        if (reservationRepository.existsByReservationTimeIdAndDate(reservationTimeId, date)) {
            throw new ReservationConflictException();
        }
    }

    private void validateExistsReservationTime(final ReservationRequest request) {
        if (!reservationTimeRepository.existsById(request.themeId())) {
            throw new ReservationNotExistsTimeException();
        }
    }

    private void validateExistsTheme(final ReservationRequest request) {
        if (!themeRepository.existsById(request.themeId())) {
            throw new ReservationNotExistsThemeException();
        }
    }
}
