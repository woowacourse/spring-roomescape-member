package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.entity.ThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;

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
                reservationTimeRepository.getById(timeId),
                themeRepository.getById(themeId)
        );
        validateReservationUniqueness(reservation);
        return reservationRepository.save(reservation);
    }

    private void validateReservationUniqueness(Reservation reservation) {
        boolean isAlreadyExist = reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.date(),
                reservation.timeId(),
                reservation.themeId()
        );

        if (isAlreadyExist) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    @Transactional
    public Reservation updateDateAndTime(Long id, String name, LocalDate newDate, Long newTimeId) {
        Reservation target = reservationRepository.getById(id);
        target.checkOwnership(name);
        ReservationTime newTime = reservationTimeRepository.getById(newTimeId);
        Reservation updated = target.update(newDate, newTime);
        updated.validateUniqueness(reservationRepository.findByDateAndThemeId(updated.date(), updated.themeId()));
        reservationRepository.update(updated);
        return updated;
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
        Reservation deleteTarget = reservationRepository.getById(id);

        deleteTarget.checkOwnership(name);
        deleteTarget.validateFuture();

        reservationRepository.deleteById(id);
    }
}
