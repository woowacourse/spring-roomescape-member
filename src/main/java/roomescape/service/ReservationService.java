package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.exception.ReservationNotFoundException;
import roomescape.domain.exception.ReservationTimeNotFoundException;
import roomescape.domain.exception.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservationTimesWithStatus;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.request.ReservationUpdateRequest;
import roomescape.service.dto.response.ReservationOptionResponse;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.service.dto.response.ThemeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final int RESERVABLE_DAYS_RANGE = 14;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getReservationsByCustomerName(final String customerName) {
        return reservationRepository.findAllByCustomerName(Name.from(customerName))
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationTimesWithStatus> getReservationTimeStatuses(final LocalDate date, final Long themeId) {
        return reservationRepository.findReservationTimeStatusesByDateAndThemeId(date, themeId);
    }

    public ReservationResponse create(final ReservationCreateRequest data) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(data.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);

        final Theme theme = themeRepository.findById(data.themeId())
                .orElseThrow(ThemeNotFoundException::new);

        final Reservation reservation = Reservation.create(
                data.name(),
                data.date(),
                reservationTime,
                theme,
                LocalDateTime.now(clock)
        );

        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    public void cancel(final Long reservationId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        reservation.validateCancelableByCustomer(LocalDate.now(clock));

        reservationRepository.deleteById(reservationId);
    }

    public void delete(final Long reservationId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        reservationRepository.deleteById(reservation.getId());
    }

    public ReservationOptionResponse getReservationOptions() {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = today.datesUntil(today.plusDays(RESERVABLE_DAYS_RANGE)).toList();

        List<ThemeResponse> themes = themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return new ReservationOptionResponse(dates, themes);
    }
}
