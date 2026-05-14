package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
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

    @Transactional(readOnly = true)
    public List<Reservation> getReservations(String requestName) {
        MemberName memberName = new MemberName(requestName);
        return reservationRepository.findAllByMemberName(memberName);
    }

    public Reservation addReservation(ReservationRequest requestDto) {
        if (!requestDto.date().isAfter(LocalDate.now())) {
            throw new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION);
        }

        ReservationTime time = reservationTimeRepository.findById(requestDto.timeId())
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(requestDto.themeId())
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        boolean alreadyExists = reservationRepository.existsByDateAndTimeIdAndThemeId(
            requestDto.date(), requestDto.timeId(), requestDto.themeId());
        if (alreadyExists) {
            throw new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    public void deleteReservation(final long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND));
        if (reservation.isBeforeNow()) {
            throw new RoomEscapeException(ErrorCode.PAST_RESERVATION_CANCEL);
        }

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

        return reservationTimeRepository.findAvailableTimeByDateAndThemeId(date, theme.getId());
    }
}
