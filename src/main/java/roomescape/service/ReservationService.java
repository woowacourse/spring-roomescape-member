package roomescape.service;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.AvailableDateResponse;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeStatusResponse;
import roomescape.service.dto.response.ThemeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    private static final int RESERVABLE_DAYS_RANGE = 14;

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationService::mapDomainToDto)
                .toList();
    }

    public List<ReservationTimeStatusResponse> getReservationTimeStatuses(final LocalDate date, final Long themeId) {
        return reservationRepository.findReservationTimeStatusesByDateAndThemeId(date, themeId)
                .stream()
                .map(reservationTimesWithStatus -> new ReservationTimeStatusResponse(
                        reservationTimesWithStatus.id(),
                        reservationTimesWithStatus.startAt(),
                        reservationTimesWithStatus.reserved()
                ))
                .toList();
    }

    public ReservationResponse create(final ReservationCreateRequest data) {
        final LocalDate date = data.date();
        validateDate(date);
        final ReservationTime reservationTime = getReservationTime(data);
        validateReservationTime(reservationTime);
        final Theme theme = getTheme(data);
        final Reservation reservation = Reservation.create(data.name(), date, reservationTime, theme);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return mapDomainToDto(savedReservation);
    }

    private void validateReservationTime(final ReservationTime reservationTime) {
        final LocalTime now = LocalTime.now(clock);
        if (reservationTime.isBefore(now)) {
            throw new IllegalArgumentException("지난 시간대로 예약이 불가합니다.");
        }
    }

    private void validateDate(final LocalDate date) {
        final LocalDate today = LocalDate.now(clock);
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("지난 날짜로 예약이 불가합니다.");
        }
    }

    private ReservationTime getReservationTime(final ReservationCreateRequest data) {
        return reservationTimeRepository.findById(data.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    private Theme getTheme(final ReservationCreateRequest data) {
        return themeRepository.findById(data.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public void delete(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }

    public AvailableDateResponse getReservationOptions() {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = today.datesUntil(today.plusDays(RESERVABLE_DAYS_RANGE)).toList();

        return new AvailableDateResponse(dates);
    }

    private static ReservationResponse mapDomainToDto(final Reservation reservation) {
        final ReservationTime reservationTime = reservation.getTime();
        final Theme theme = reservation.getTheme();

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt(),
                        reservationTime.getEndAt()
                ),
                new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnailUrl()
                )
        );
    }
}
