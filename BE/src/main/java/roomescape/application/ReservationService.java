package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.global.exception.ReservationErrorCode;
import roomescape.global.exception.ReservationTimeErrorCode;
import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.exception.customException.BadRequestException;
import roomescape.global.exception.customException.BusinessException;
import roomescape.global.exception.customException.EntityNotFoundException;

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
    public Reservation saveReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(ThemeErrorCode.THEME_NOT_FOUND));
        validateSaveReservation(date, time, themeId);
        Reservation reservation = Reservation.create(
                name,
                date,
                time,
                theme
        );
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByDateAndTheme(LocalDate date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    public List<Reservation> getReservationsByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public void updateReservationSchedule(LocalDate date, Long timeId, Long id, String name) {
        validateId(id);
        validateName(name);
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND));
        Reservation pastReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        validateUpdateReservation(date, time, pastReservation, name);
        reservationRepository.updateScheduleByIdAndName(date, timeId, id, name);
    }

    @Transactional
    public void deleteReservation(Long id) {
        validateId(id);
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteReservationByName(Long id, String name) {
        validateId(id);
        validateName(name);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        validateOwnerForDelete(reservation, name);
        validateNotPastReservation(reservation);
        reservationRepository.deleteByIdAndName(id, name);
    }

    private void validateSaveReservation(
            LocalDate date,
            ReservationTime timeId,
            Long themeId
    ) {
        validateAlreadyReservation(date, timeId.getId(), themeId);
        validatePastDateReservation(date);
        validatePastTimeReservation(date, timeId);
    }

    private void validateUpdateReservation(
            LocalDate date,
            ReservationTime time,
            Reservation pastReservation,
            String name
    ) {
        validateOwner(pastReservation, name);
        validatePastDateReservation(date);
        validatePastTimeReservation(date, time);
        validateAlreadyReservationExcludingSelf(
                date,
                time.getId(),
                pastReservation.getTheme().getId(),
                pastReservation.getId()
        );
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_ID_REQUIRED);
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException(ReservationErrorCode.RESERVATION_NAME_REQUIRED);
        }
    }

    private void validateAlreadyReservation(
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        boolean exists = reservationRepository
                .findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .isPresent();

        if (exists) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_EXISTS);
        }
    }

    private void validatePastDateReservation(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_PAST_DATE);
        }
    }

    private void validatePastTimeReservation(LocalDate date, ReservationTime time) {
        if (date.isEqual(LocalDate.now()) && time.getStartAt().isBefore(LocalTime.now())) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_PAST_DATE);
        }
    }

    private void validateOwner(Reservation reservation, String name) {
        if (!reservation.getName().equals(name)) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_OWNER_MISMATCH);
        }
    }

    private void validateAlreadyReservationExcludingSelf(
            LocalDate date,
            Long timeId,
            Long themeId,
            Long reservationId
    ) {
        reservationRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .filter(foundReservation ->
                        !foundReservation.getId().equals(reservationId)
                )
                .ifPresent(reservation -> {
                    throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_EXISTS);
                });
    }

    private void validateOwnerForDelete(Reservation reservation, String name) {
        if (!reservation.getName().equals(name)) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_OWNER_MISMATCH);
        }
    }

    private void validateNotPastReservation(Reservation reservation) {
        LocalDate date = reservation.getDate();
        LocalTime time = reservation.getTime().getStartAt();

        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_PAST);
        }

        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_PAST);
        }
    }

}
