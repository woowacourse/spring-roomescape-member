package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        Reservation reservation = Reservation.createWithNullId(
                name,
                date,
                findTargetTimeById(timeId),
                findTargetThemeById(themeId)
        );

        reservation.validateUniqueness(
                reservationRepository.findByDateAndThemeId(reservation.date(), reservation.theme().id()));

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateDateAndTime(Long id, String name, LocalDate date, Long timeId) {
        Reservation updated = createNewReservation(id, name, date, timeId);
        updated.validateUniqueness(reservationRepository.findByDateAndThemeId(updated.date(), updated.theme().id()));
        reservationRepository.update(updated);
        return updated;
    }

    private Reservation createNewReservation(Long id, String name, LocalDate date, Long timeId) {
        Reservation target = findById(id);
        target.checkOwnership(name);

        LocalDate newDate = decideNewLocalDateValue(date, target.date());
        ReservationTime newTime = decideNewReservationTimeValue(timeId, target.time());

        return target.update(newDate, newTime);
    }

    private ReservationTime decideNewReservationTimeValue(Long timeId, ReservationTime originReservationTime) {
        if (timeId != null) {
            return findTargetTimeById(timeId);
        }
        return originReservationTime;
    }

    private LocalDate decideNewLocalDateValue(LocalDate newDate, LocalDate originDate) {
        if (newDate != null) {
            return newDate;
        }
        return originDate;
    }

    private Theme findTargetThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }

    private ReservationTime findTargetTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND_BY_ID));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(Long id, String name) {
        Reservation deleteTarget = reservationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND_BY_ID)
        );

        deleteTarget.checkOwnership(name);
        deleteTarget.validateFuture();

        reservationRepository.deleteById(id);
    }
}
