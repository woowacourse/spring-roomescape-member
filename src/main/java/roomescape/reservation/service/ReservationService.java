package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationCreateInfo;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.user.model.User;
import roomescape.user.service.UserService;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final ThemeRepository themeRepository;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, ThemeRepository themeRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.themeRepository = themeRepository;
        this.userService = userService;
    }

    @Transactional
    public ReservationIdResponse create(ReservationCreateInfo info) {
        User user = userService.getUserById(info.userId());

        Theme theme = themeRepository.findById(info.themeId());

        LocalDateTime startAt = LocalDateTime.of(LocalDate.parse(info.date()), LocalTime.parse(info.time()));

        Schedule schedule = scheduleRepository.findByThemeIdAndStartAt(theme.getId(), startAt)
                .orElseThrow(() -> new IllegalArgumentException("등록된 스케줄이 없습니다."));

        if (reservationRepository.existsByScheduleId(schedule.getId())) {
            throw new IllegalArgumentException("해당 시간은 이미 예약이 완료되었습니다.");
        }

        Reservation reservation = new Reservation(user, schedule, theme);
        Long reservationId = reservationRepository.create(reservation);
        Reservation savedReservation = new Reservation(reservationId, user, schedule, theme);
        return ReservationIdResponse.from(savedReservation);
    }

    public ReservationsResponse findAll() {
        List<Reservation> responses = reservationRepository.findAll();
        return ReservationsResponse.from(responses);
    }

    @Transactional
    public void delete(long id) {
        reservationRepository.delete(id);
    }
}
