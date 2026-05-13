package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicationException;
import roomescape.exception.NotFoundException;
import roomescape.exception.code.ConflictCode;
import roomescape.exception.code.NotFoundCode;
import roomescape.policy.ReservationSavePolicy;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    public Reservation saveReservation(ReservationSaveCommand command, LocalDateTime now, ReservationSavePolicy policy) {
        checkIfReservationPossible(command);
        ReservationTime reservationTime = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.THEME_NOT_FOUND));

        policy.validate(command, reservationTime, now);

        return reservationRepository.addReservation(Reservation.forSave(command, reservationTime, theme));
    }

    private void checkIfReservationPossible(ReservationSaveCommand command) {
        if (reservationRepository.countReservationsOf(command.date(), command.timeId(), command.themeId()) > 0) {
            throw new DuplicationException(ConflictCode.RESERVATION_DUPLICATED);
        }
    }

    public List<Reservation> findReservationsByName(String name) {
        return reservationRepository.findReservationsByName(name);
    }
}
