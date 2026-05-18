package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;
import roomescape.service.command.ReservationCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation addReservation(ReservationCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));

        List<ReservationTime> availableTimes = reservationTimeRepository
                .findAvailableTimes(command.date(), command.themeId());

        if (!availableTimes.contains(time)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }

        Reservation reservation = Reservation.create(command.name(), command.date(), time, theme);
        return reservationRepository.createReservation(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime addReservationTime(ReservationTime time) {
        return reservationTimeRepository.createReservationTime(time);
    }

    @Transactional
    public void deleteReservation(Long id, MemberName memberName) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getName().equals(memberName)) {
            throw new BusinessException(ErrorCode.RESERVATION_ACCESS_DENIED);
        }

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new BusinessException(ErrorCode.TIME_IN_USE);
        }

        reservationTimeRepository.deleteById(id);
    }

    public Map<ReservationTime, Boolean> getTimesWithAvailability(ReservationDate date, Long themeId) {
        Map<ReservationTime, Boolean> timesWithAvailability = new HashMap<>();

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));

        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimes(date, theme.getId());

        for (ReservationTime time : reservationTimeRepository.findAll()) {
            if (availableTimes.contains(time)) {
                timesWithAvailability.put(time, true);
                continue;
            }

            timesWithAvailability.put(time, false);
        }

        return timesWithAvailability;
    }

    public List<Reservation> findReservationsByName(MemberName name) {
        return reservationRepository.findReservationsByName(name);
    }

    @Transactional
    public void update(Long id, ReservationCommand command, MemberName name) {
        Reservation oldReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!oldReservation.getName().equals(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_ACCESS_DENIED);
        }

        List<Long> availableTimeIds = reservationTimeRepository
                .findAvailableTimes(command.date(), command.themeId()).stream()
                .map(ReservationTime::getId)
                .toList();

        if (oldReservation.getTime().getId() != command.timeId() && !availableTimeIds.contains(command.timeId())) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_CONFLICT);
        }

        reservationRepository.update(
                Reservation.create(
                                command.name(),
                                command.date(),
                                reservationTimeRepository.findById(command.timeId()).orElseThrow(() -> new BusinessException(ErrorCode.TIME_NOT_FOUND)),
                                themeRepository.findById(command.themeId()).orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND)))
                        .withId(id));
    }
}
