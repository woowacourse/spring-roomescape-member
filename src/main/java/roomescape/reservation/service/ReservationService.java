package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationDateException;
import roomescape.reservation.exception.NotReservationOwnerException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.PopularThemesResult;
import roomescape.reservation.service.dto.ReservationCommand;
import roomescape.reservation.service.dto.ReservationUpdateCommand;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.InvalidTimeStartAtException;
import roomescape.time.exception.TimeNotFoundException;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public Reservation makeReservation(ReservationCommand command) {
        ReservationTime time = getReservationTime(command.timeId());

        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(ThemeNotFoundException::new);

        validate(command.date(), time.getStartAt());

        return reservationRepository.save(
                Reservation.of(command.name(), command.date(), time, theme)
        );
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(TimeNotFoundException::new);
    }

    private void validate(LocalDate date, LocalTime startAt) {
        LocalDate nowDate = LocalDate.now(clock);

        if (nowDate.isAfter(date)) {
            throw new InvalidReservationDateException();
        }

        if (nowDate.equals(date) && LocalTime.now(clock).isAfter(startAt)) {
            throw new InvalidTimeStartAtException();
        }
    }

    @Transactional
    public void deleteReservationById(Long id) {
        Reservation reservation = getReservation(id);
        validate(reservation.getDate(), reservation.getTime().getStartAt());
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void updateReservation(ReservationUpdateCommand command, Long id) {
        Reservation reservation = getReservation(id);

        validate(
                reservation.getDate(),
                reservation.getTime().getStartAt()
        );

        Reservation updated = updateField(command, reservation);

        validate(
                updated.getDate(),
                updated.getTime().getStartAt()
        );

        reservationRepository.update(updated);
    }

    private Reservation updateField(ReservationUpdateCommand command, Reservation reservation) {
        Reservation result = reservation;

        if (command.date() != null) {
            result = reservation.updateDate(command.date());
        }

        if (command.timeId() != null) {
            result = result.updateTime(
                    getReservationTime(command.timeId())
            );
        }

        return result;
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);
    }

    public List<Reservation> findReservationsByName(String name) {
        return reservationRepository.findAllByName(name);
    }

    public List<Reservation> findReservations() {
        return reservationRepository.findAll();
    }

    public PopularThemesResult findPopularThemes(int period, int limit) {
        LocalDate to = LocalDate.now(clock).minusDays(1);
        LocalDate from = to.minusDays(period - 1);

        return new PopularThemesResult(
                reservationRepository.findPopularThemes(from, to, limit)
        );
    }

    public void authorizeOwner(String name, Long id) {
        Reservation reservation = getReservation(id);

        if (!reservation.getName().equals(name)) {
            throw new NotReservationOwnerException();
        }
    }
}
