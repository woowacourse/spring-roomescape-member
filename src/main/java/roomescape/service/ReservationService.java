package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationUpdateRequest;
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
        try {
            return reservationRepository.createReservation(reservation);
        } catch (DuplicateKeyException e) {
            throw new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        }
    }

    public void updateDateTime(long reservationId, String requestName, ReservationUpdateRequest request) {
        if (!request.date().isAfter(LocalDate.now())) {
            throw new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION);
        }

        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND));
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new RoomEscapeException(ErrorCode.TIME_NOT_FOUND));

        boolean alreadyExists = reservationRepository.existsByDateAndTimeIdAndThemeId(
            request.date(), request.timeId(), reservation.getThemeId());
        if (alreadyExists) {
            throw new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        }

        MemberName name = new MemberName(requestName);
        if (!reservation.isBooker(name)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }
        if (reservation.isBeforeNow()) {
            throw new RoomEscapeException(ErrorCode.PAST_RESERVATION_UPDATE);
        }

        int version = reservationRepository.findVersionById(reservationId);
        Reservation updated = reservation.updateDateTime(request.date(), reservationTime);
        try {
            int affected = reservationRepository.updateById(updated, version);
            if (affected == 0) {
                if (!reservationRepository.existsById(reservationId)) {
                    throw new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND);
                }
                throw new RoomEscapeException(ErrorCode.RESERVATION_CONCURRENT_MODIFICATION);
            }
        } catch (DuplicateKeyException e) {
            throw new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        }
    }

    public void deleteReservation(final long id, String requestName) {
        Optional<Reservation> saved = reservationRepository.findById(id);
        if (saved.isEmpty()) {
            return;
        }

        Reservation reservation = saved.get();
        MemberName name = new MemberName(requestName);
        if (!reservation.isBooker(name)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }

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
