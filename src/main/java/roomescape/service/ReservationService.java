package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.dto.ReservationCreateData;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationOptionResponse;
import roomescape.service.dto.response.ReservationResponse;
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
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationTimeStatusResponse> getReservationTimeStatuses(final LocalDate date, final Long themeId) {
        return reservationRepository.findReservationTimeStatusesByDateAndThemeId(date, themeId)
                .stream()
                .map(ReservationTimeStatusResponse::from)
                .toList();
    }

    public ReservationResponse create(final ReservationCreateRequest data) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(data.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        final Theme theme = themeRepository.findById(data.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        final Reservation reservation = Reservation.create(
                new ReservationCreateData(
                        data.name(),
                        data.date(),
                        reservationTime,
                        theme
                )
        );

        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    public void delete(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
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
