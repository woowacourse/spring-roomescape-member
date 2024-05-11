package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.ReservationCreationRequest;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;

@Service
public class ReservationService {
    private static final int IN_ADVANCE_RESERVATION_DAYS = 1;

    private final Clock clock;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(Clock clock, ReservationTimeRepository reservationTimeRepository,
                              ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.clock = clock;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse reserve(ReservationCreationRequest request) {
        ReservationTime time = getTime(request.timeId());
        validateReservationInAdvance(request.date(), time.getStartAt());
        Theme theme = getTheme(request.themeId());
        Reservation reservation = reservationRepository.save(request.toReservation(time, theme));
        return ReservationResponse.from(reservation);
    }

    private void validateReservationInAdvance(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        LocalDateTime baseDateTime = LocalDateTime.now(clock).plusDays(IN_ADVANCE_RESERVATION_DAYS);
        if (reservationDateTime.isBefore(baseDateTime)) {
            System.out.println(LocalDateTime.now(clock));
            System.out.println(baseDateTime);
            System.out.println(reservationDateTime);
            throw new IllegalArgumentException(String.format("예약은 최소 %d일 전에 해야합니다.", IN_ADVANCE_RESERVATION_DAYS));
        }
    }

    private ReservationTime getTime(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void cancel(long id) {
        boolean isDeleted = reservationRepository.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }
}
