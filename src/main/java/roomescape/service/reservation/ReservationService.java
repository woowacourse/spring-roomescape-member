package roomescape.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.exception.ConflictException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.service.reservationtime.ReservationTimeService;
import roomescape.domain.theme.Theme;
import roomescape.service.theme.ThemeService;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeService reservationTimeService,
            final ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getAllByName(final String name) {
        validateReservationName(name);
        return reservationRepository.findAllByName(name);
    }

    public Reservation save(final String name, final LocalDate date, final Long themeId, final Long timeId) {
        if (themeId == null) {
            throw new InvalidInputException("THEME_ID_REQUIRED", "themeId는 필수입니다.");
        }

        if (timeId == null) {
            throw new InvalidInputException("RESERVATION_TIME_ID_REQUIRED", "timeId는 필수입니다.");
        }

        if (date == null) {
            throw new InvalidInputException("RESERVATION_DATE_REQUIRED", "날짜는 필수입니다.");
        }

        Theme theme = themeService.getById(themeId);
        ReservationTime reservationTime = reservationTimeService.getById(timeId);
        LocalDateTime reservationDateTime = LocalDateTime.of(date, reservationTime.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("RESERVATION_DATE_TIME_IN_PAST", "과거 날짜와 시간으로는 예약을 할 수 없습니다.");
        }

        if(reservationRepository.existsByDateAndThemeIdAndTimeId(date, themeId, timeId)){
            throw new ConflictException("RESERVATION_DUPLICATED", "동일한 시기에 예약을 할 수 없습니다.");
        }

        Reservation nonIdReservation = Reservation.createNew(name, date, theme, reservationTime);
        return reservationRepository.save(nonIdReservation);
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

    public void deleteByIdAndName(final long id, final String name) {
        validateReservationName(name);

        Reservation reservation = reservationRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MY_RESERVATION_NOT_FOUND",
                        "조회한 이름으로 찾은 예약이 없습니다."
                ));

        reservationRepository.deleteById(reservation.getId());
    }

    public Reservation updateByIdAndName(
            final long id,
            final String name,
            final LocalDate date,
            final Long timeId
    ) {
        validateReservationName(name);

        if (timeId == null) {
            throw new InvalidInputException("RESERVATION_TIME_ID_REQUIRED", "timeId는 필수입니다.");
        }

        if (date == null) {
            throw new InvalidInputException("RESERVATION_DATE_REQUIRED", "날짜는 필수입니다.");
        }

        Reservation reservation = reservationRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MY_RESERVATION_NOT_FOUND",
                        "조회한 이름으로 찾은 예약이 없습니다."
                ));

        ReservationTime reservationTime = reservationTimeService.getById(timeId);
        LocalDateTime reservationDateTime = LocalDateTime.of(date, reservationTime.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("RESERVATION_DATE_TIME_IN_PAST", "과거 날짜와 시간으로는 예약을 할 수 없습니다.");
        }

        if (reservationRepository.existsByDateAndThemeIdAndTimeIdExcludingId(
                date,
                reservation.getTheme().getId(),
                timeId,
                reservation.getId()
        )) {
            throw new ConflictException("RESERVATION_DUPLICATED", "동일한 시기에 예약을 할 수 없습니다.");
        }

        Reservation updatedReservation = reservation.withDateAndTime(date, reservationTime);
        return reservationRepository.update(updatedReservation);
    }

    private void validateReservationName(final String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("RESERVATION_NAME_REQUIRED", "예약자 이름은 필수입니다.");
        }
    }

}
