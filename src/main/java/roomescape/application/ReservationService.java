package roomescape.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservation.ReservationRequest;

@Service
public class ReservationService {
    private static final int IN_ADVANCE_RESERVATION_DAYS = 1;

    private final ReservationTimeService reservationTimeService;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationTimeService reservationTimeService,
                              ReservationRepository reservationRepository,
                              ThemeRepository themeRepository) {
        this.reservationTimeService = reservationTimeService;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation reserve(ReservationRequest request) {
        ReservationTime time = reservationTimeService.getReservationTime(request.timeId());
        validateReservationInAdvance(request.date(), time.getStartAt());
        if (reservationRepository.existsByReservationDateTimeAndTheme(request.date(), time.getId(),
                request.themeId())) {
            throw new IllegalArgumentException("이미 예약된 날짜, 시간입니다.");
        }
        Reservation reservation = new Reservation(request.name(), request.date(), time, getTheme(request.themeId()));
        return reservationRepository.save(reservation);
    }

    private void validateReservationInAdvance(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        LocalDateTime baseDateTime = LocalDateTime.now().plusDays(IN_ADVANCE_RESERVATION_DAYS);
        if (reservationDateTime.isBefore(baseDateTime)) {
            throw new IllegalArgumentException(String.format("예약은 최소 %d일 전에 해야합니다.", IN_ADVANCE_RESERVATION_DAYS));
        }
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public void cancel(long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
        reservationRepository.deleteById(id);
    }
}
