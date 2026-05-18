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
        final ReservationTime reservationTime = getReservationTime(data.timeId());
        final Theme theme = getTheme(data.themeId());

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

    public ReservationResponse updateByCustomer(final Long reservationId, final ReservationUpdateRequest data) {
        final Reservation originReservation = getReservation(reservationId);
        originReservation.validateModifiableByCustomer(LocalDate.now(clock));

        return updateSchedule(data, originReservation);
    }

    public ReservationResponse updateByAdmin(final Long reservationId, final ReservationUpdateRequest data) {
        final Reservation originReservation = getReservation(reservationId);

        return updateSchedule(data, originReservation);
    }

    public void cancel(final Long reservationId) {
        final Reservation reservation = getReservation(reservationId);

        reservation.validateCancelableByCustomer(LocalDate.now(clock));

        deleteReservation(reservationId);
    }

    public void delete(final Long reservationId) {
        deleteReservation(reservationId);
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

    private ReservationResponse updateSchedule(final ReservationUpdateRequest data, final Reservation originReservation) {
        final ReservationTime newReservationTime = getReservationTime(data.timeId());

        final Reservation updatedReservation = originReservation.changeSchedule(
                data.date(),
                newReservationTime,
                LocalDateTime.now(clock)
        );
        final Reservation reservation = reservationRepository.update(updatedReservation);

        return ReservationResponse.from(reservation);
    }

    private void deleteReservation(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new ReservationNotFoundException();
        }
    }

    private Reservation getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
    }

    private ReservationTime getReservationTime(final Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(ReservationTimeNotFoundException::new);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);
    }
}
