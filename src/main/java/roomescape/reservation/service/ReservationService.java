package roomescape.reservation.service;

import static roomescape.date.exception.ReservationDateExceptionInformation.DATE_NOT_FOUND;
import static roomescape.reservation.domain.ReservationStatus.CANCELED;
import static roomescape.theme.exception.ThemeExceptionInformation.THEME_NOT_FOUND;
import static roomescape.time.exception.ReservationTimeExceptionInformation.TIME_NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.date.domain.ReservationDate;
import roomescape.date.exception.ReservationDateException;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.ReservationTimeException;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
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

    public List<Reservation> readAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> readAllByName(String name) {
        return reservationRepository.findAllByNameOrderByDateAndTime(name);
    }

    @Transactional
    public Reservation reserve(ReservationSaveDto dto) {
        ReservationTime reservationTime = getReservationTime(dto.timeId());
        ReservationDate reservationDate = getReservationDate(dto.dateId());
        Theme theme = getTheme(dto.themeId());

        validateNotAlreadyBookedByOthers(reservationDate.getId(), reservationTime.getId(), theme);
        return reservationRepository.save(
                Reservation.create(dto.name(), reservationDate, reservationTime, theme)
        );
    }

    @Transactional
    public Reservation cancelByManager(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateStatus(CANCELED);
        reservationRepository.updateStatus(reservation);
        return reservation;
    }

    @Transactional
    public Reservation cancel(Long id, String requesterName) {
        Reservation reservation = getReservation(id);
        reservation.cancel(requesterName);
        reservationRepository.updateStatus(reservation);
        return reservation;
    }

    // 파라미터 Dto 고려
    @Transactional
    public Reservation changeSchedule(Long id, String requesterName, Long dateId, Long timeId) {
        Reservation reservation = getReservation(id);
        ReservationDate newDate = getReservationDate(dateId);
        ReservationTime newTime = getReservationTime(timeId);

        reservation.changeSchedule(requesterName, newDate, newTime);
        reservationRepository.updateSchedule(reservation);
        return reservation;
    }

    @Transactional
    public Reservation changeScheduleByManager(Long id, Long dateId, Long timeId) {
        Reservation reservation = getReservation(id);
        ReservationDate newDate = getReservationDate(dateId);
        ReservationTime newTime = getReservationTime(timeId);

        reservation.changeScheduleByManager(newDate, newTime);
        reservationRepository.updateSchedule(reservation);
        return reservation;
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException(TIME_NOT_FOUND));
    }

    private ReservationDate getReservationDate(Long dateId) {
        return reservationDateRepository.findById(dateId)
                .orElseThrow(() -> new ReservationDateException(DATE_NOT_FOUND));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeException(THEME_NOT_FOUND));
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
    }

    private void validateNotAlreadyBookedByOthers(Long dateId, Long timeId, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(dateId, timeId, theme.getId())) {
            throw new IllegalArgumentException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

}
