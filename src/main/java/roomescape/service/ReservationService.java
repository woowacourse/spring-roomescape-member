package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationSaveCommand;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAllReservations();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation saveReservation(ReservationSaveCommand command) {
        ReservationTime reservationTime = findReservationTime(command);
        validateReservableDateTime(command.date(), reservationTime.startAt());
        Theme theme = findTheme(command);

        Reservation reservation = new Reservation(null, command.name(), command.date(), reservationTime, theme);

        return reservationRepository.addReservation(reservation);
    }

    private void validateReservableDateTime(LocalDate date, LocalTime startAt) {
        if (date == null || startAt == null) {
            throw new IllegalArgumentException("유효하지 않은 예약 날짜 또는 시간입니다.");
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지난 날짜와 시간은 예약할 수 없습니다.");
        }
    }

    @NonNull
    private ReservationTime findReservationTime(ReservationSaveCommand command) {
        return reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    @NonNull
    private Theme findTheme(ReservationSaveCommand command) {
        return themeRepository.findById(command.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public List<Reservation> findReservationsByName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        List<Reservation> reservations = reservationRepository.findReservationsByName(name);
        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("예약을 찾을 수 없습니다.");
        }

        return reservations;
    }
}
