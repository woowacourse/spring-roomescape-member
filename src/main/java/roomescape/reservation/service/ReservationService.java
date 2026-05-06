package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.user.model.User;
import roomescape.user.model.Role;
import roomescape.user.repository.UserRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, UserRepository userRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationIdResponse create(ReservationRequest request) {
        User user = userRepository.findByName(request.name())
                .orElseGet(() -> {
                    User newUser = new User(request.name(), Role.USER);
                    Long newUserId = userRepository.create(newUser);
                    return new User(newUserId, request.name(), Role.USER);
                });

        Theme theme = themeRepository.findById(request.themeId());

        LocalDateTime startAt = LocalDateTime.of(LocalDate.parse(request.date()), LocalTime.parse(request.time()));

        // 🚨 과거 날짜/시간 예약 방지 로직 추가
        if (startAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 날짜/시간에는 예약할 수 없습니다.");
        }

        Schedule schedule = new Schedule(startAt, theme);
        Long scheduleId = scheduleRepository.create(schedule);
        Schedule savedSchedule = new Schedule(scheduleId, startAt, theme);

        Reservation reservation = new Reservation(user, savedSchedule, theme);
        Long reservationId = reservationRepository.create(reservation);
        Reservation savedReservation = new Reservation(reservationId, user, schedule, schedule.getTheme());
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
