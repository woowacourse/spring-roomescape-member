package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservationTime.ReservationTimeRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation addReservation(ReservationRequest requestDto) {
        ReservationTime time = reservationTimeRepository.findById(requestDto.timeId())
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(requestDto.themeId())
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        List<ReservationTime> availableTimes = reservationTimeRepository
            .findByDateAndThemeId(requestDto.date(), requestDto.themeId());
        if (!availableTimes.contains(time)) {
            throw new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    public void deleteReservation(final long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeRequest requestDto) {
        return reservationTimeRepository.createReservationTime(
            new ReservationTime(requestDto.startAt()));
    }

    public void deleteReservationTime(final long id) {
        LocalDate today = LocalDate.now();
        if (reservationRepository.existsByTimeIdAndDateOnOrAfter(id, today)) {
            throw new RoomEscapeException(ErrorCode.TIME_HAS_RESERVATIONS);
        }

        reservationTimeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> getAvailableTimes(LocalDate date, final long themeId) {
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        return reservationTimeRepository.findByDateAndThemeId(date, theme.getId());
    }
}
