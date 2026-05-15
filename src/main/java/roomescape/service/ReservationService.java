package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservedTimes;
import roomescape.service.dto.reservation.CreateReservationCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.reservation.InvalidReservationException;
import roomescape.global.exception.reservation.ReservationNotFoundException;
import roomescape.global.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.global.exception.theme.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.reservation.ReservationPagingCondition;
import roomescape.service.dto.reservation.ReservationResult;
import roomescape.service.dto.reservation.ChangeReservationScheduleCommand;
import roomescape.service.dto.reservation.CancelReservationCommand;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public List<ReservationResult> getReservations(ReservationPagingCondition condition) {
        return reservationRepository.findAll(condition.size(), condition.offset()).stream()
                .map(ReservationResult::from)
                .toList();
    }

    public List<ReservationResult> getReservationsByName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(ReservationResult::from)
                .toList();
    }

    @Transactional
    public ReservationResult createReservation(CreateReservationCommand command) {
        ReservationTime time = getReservationTime(command);
        Theme theme = getTheme(command);
        validateReservableDateTime(command.date(), time);
        ReservedTimes reservedTimes = new ReservedTimes(reservationTimeRepository.findReservedTimeIds(
                theme.getId(),
                command.date()
        ));
        reservedTimes.validateAvailable(time.getId());

        Reservation reservation = reservationRepository.save(
                Reservation.createNew(
                        command.name(),
                        command.date(),
                        time,
                        theme)
        );

        return ReservationResult.from(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResult changeReservationSchedule(ChangeReservationScheduleCommand command) {
        Reservation reservation = getReservation(command.reservationId(), command.name());
        ReservationTime time = getReservationTime(command.timeId());
        validateReservableDateTime(command.date(), time);

        Reservation changedReservation = reservation.changeSchedule(command.date(), time);
        return ReservationResult.from(reservationRepository.updateSchedule(changedReservation));
    }

    @Transactional
    public ReservationResult cancelReservation(CancelReservationCommand command) {
        Reservation reservation = getReservation(command.reservationId(), command.name());
        Reservation cancelledReservation = reservation.cancel();
        return ReservationResult.from(reservationRepository.updateStatus(cancelledReservation));
    }

    @NonNull
    private ReservationTime getReservationTime(CreateReservationCommand command) {
        return getReservationTime(command.timeId());
    }

    @NonNull
    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException("선택한 예약 시간이 존재하지 않습니다."));
    }

    @NonNull
    private Theme getTheme(CreateReservationCommand command) {
        return themeRepository.findById(command.themeId())
                .orElseThrow(() -> new ThemeNotFoundException("선택한 테마가 존재하지 않습니다."));
    }

    @NonNull
    private Reservation getReservation(Long reservationId, String name) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("해당 예약을 찾을 수 없습니다."));
        if (!reservation.getName().equals(name)) {
            throw new ReservationNotFoundException("해당 예약을 찾을 수 없습니다.");
        }
        return reservation;
    }

    private void validateReservableDateTime(LocalDate date, ReservationTime time) {
        LocalDate today = LocalDate.now(clock);
        LocalTime now = LocalTime.now(clock);

        if (date.isBefore(today) || date.isEqual(today) && time.getStartAt().isBefore(now)) {
            throw new InvalidReservationException("과거 날짜/시간으로는 예약할 수 없습니다.");
        }
        if (date.isAfter(today.plusDays(30))) {
            throw new InvalidReservationException("30일을 초과한 날짜로는 예약할 수 없습니다.");
        }
    }
}
