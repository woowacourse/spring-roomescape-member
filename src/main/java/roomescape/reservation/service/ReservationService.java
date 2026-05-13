package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AuthorizationException;
import roomescape.exception.ResourceNotFoundException;
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
        Theme theme = themeRepository.findById(info.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Schedule schedule = scheduleRepository.findByThemeIdAndStartAt(theme.getId(), info.startAt())
                .orElseThrow(() -> new IllegalArgumentException("등록된 스케줄이 없습니다."));

        validateReservation(schedule);

        Reservation reservation = new Reservation(user, schedule, theme);
        Reservation savedReservation = reservationRepository.create(reservation);
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

    private void validateReservation(Schedule schedule) {
        if (schedule.getStartAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 날짜/시간에는 스케줄을 생성할 수 없습니다.");
        }

        if (reservationRepository.existsByScheduleId(schedule.getId())) {
            throw new IllegalArgumentException("해당 시간은 이미 예약이 완료되었습니다.");
        }
    }

    public ReservationsResponse findAllByUserId(Long id) {
        userService.getUserById(id);
        List<Reservation> responses = reservationRepository.findAllByUserId(id);
        return ReservationsResponse.from(responses);
    }

    @Transactional
    public void cancel(Long reservationId, Long currentUserId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약입니다."));

        if (!reservation.isOwnedBy(currentUserId)) {
            throw new AuthorizationException("예약을 취소할 권한이 없습니다.");
        }

        reservationRepository.delete(reservationId);
    }
}
