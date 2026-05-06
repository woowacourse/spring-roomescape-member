package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    public Reservation saveUserReservation(ReservationSaveCommand command) {
        if (isPast(command)) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }

        return saveReservation(command);
    }

    private boolean isPast(ReservationSaveCommand command) {
        return Objects.nonNull(command.date()) && command.date().isBefore(LocalDate.now());
    }

    public Reservation saveReservation(ReservationSaveCommand command) {
        ReservationTime reservationTime = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new NotFoundException("reservation"));

        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new NotFoundException("theme"));
        Reservation reservation = new Reservation(null, command.name(), command.date(), reservationTime, theme);

        return reservationRepository.addReservation(reservation);
    }

    public List<Reservation> findReservationsByName(String name) {
        if (Objects.isNull(name)) {
            throw new NotFoundException("username");
        }
        List<Reservation> reservations = reservationRepository.findReservationsByName(name);
        if (reservations.isEmpty()) {
            throw new NotFoundException("reservation");
        }

        return reservations;
    }
}
