package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.CANCELED;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ReservationDateRepository reservationDateRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationDateRepository = reservationDateRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Reservation> readAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> readAllByName(String name) {
        return reservationRepository.findAllByNameOrderByDateAndTime(name);
    }

    public Reservation reserve(ReservationSaveDto dto) {
        ReservationTime reservationTime = getReservationTime(dto.timeId());
        ReservationDate reservationDate = getReservationDate(dto.dateId());
        Theme theme = getTheme(dto.themeId());

        validateNotAlreadyBookedByOthers(reservationDate.date(), reservationTime.startAt(), theme);
        validateUserHasNoReservationAtSameTime(dto.name(), reservationDate, reservationTime);
        return reservationRepository.save(
                Reservation.create(dto.name(), reservationDate.date(), reservationTime.startAt(), theme)
        );
    }

    public Reservation cancel(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateStatus(CANCELED);
        reservationRepository.updateStatus(reservation);
        return reservation;
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
    }

    private ReservationDate getReservationDate(Long dateId) {
        return reservationDateRepository.findById(dateId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 날짜입니다."));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id())) {
            throw new ConflictException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

    private void validateUserHasNoReservationAtSameTime(String name, ReservationDate date, ReservationTime time) {
        if (reservationRepository.existsByNameAndDateAndTime(name, date.date(), time.startAt())) {
            throw new ConflictException("동일한 날짜와 시간에 예약이 존재합니다.");
        }
    }

}
