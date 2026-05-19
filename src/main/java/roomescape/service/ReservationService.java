package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationUpdateRequestDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Transactional(readOnly = true)
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponseDto create(ReservationRequestDto request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateFutureDateTime(request.date(), reservationTime);
        validateUniqueForCreate(request.date(), reservationTime.getId(), theme.getId());

        Reservation reservationWithoutId = request.toEntity(reservationTime, theme);
        Reservation reservation = reservationRepository.create(reservationWithoutId);

        return ReservationResponseDto.from(reservation);
    }

    private void validateFutureDateTime(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.RESERVATION_DATE_PASSED);
        }
        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_PASSED);
        }
    }

    private void validateUniqueForCreate(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new CustomException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    public List<ReservationResponseDto> findReservation(String name) {
        if (name == null) {
            return findAll();
        }
        return findAllByName(name);
    }

    private List<ReservationResponseDto> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    private List<ReservationResponseDto> findAllByName(String name) {
        return reservationRepository.findAllByName(name).stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    @Transactional
    public void update(Long reservationId, ReservationUpdateRequestDto request) {
        Reservation reservation = getReservation(reservationId);
        validateFutureReservation(reservation);
        ReservationTime time = getReservationTime(request.timeId());
        validateFutureDateTime(request.date(), time);

        validateUniqueForUpdate(request.date(), time.getId(), reservation.getThemeId(), reservationId);

        Reservation updated = reservation.changeSchedule(request.date(), time);
        reservationRepository.update(updated);
    }

    private void validateUniqueForUpdate(LocalDate date, Long timeId, Long themeId, Long excludeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeIdExcludingId(date, timeId, themeId, excludeId)) {
            throw new CustomException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = getReservation(id);
        validateFutureReservation(reservation);
        reservationRepository.delete(id);
    }

    private void validateFutureReservation(Reservation reservation) {
        if (reservation.isPast(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_ALREADY_PAST);
        }
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme getTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
    }
}
