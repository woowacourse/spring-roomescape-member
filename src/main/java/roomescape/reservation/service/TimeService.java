package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.error.exception.BadRequestException;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;
import roomescape.reservation.dto.request.TimeRequest;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.dto.response.TimeResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.Time;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(
            TimeRepository timeRepository,
            ReservationRepository reservationRepository
    ) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public TimeResponse create(TimeRequest request) {
        Time time = request.toEntity();
        validateOperatingTime(time);
        validateDuplicated(time);
        Time saved = timeRepository.save(time);
        return TimeResponse.from(saved);
    }

    private void validateOperatingTime(Time entity) {
        if (!entity.isAvailable()) {
            throw new BadRequestException("운영 시간 이외의 날짜는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicated(Time entity) {
        List<Time> times = timeRepository.findAll();
        if (times.stream().anyMatch(time -> time.isDuplicatedWith(entity))) {
            throw new ConflictException("러닝 타임이 겹치는 시간이 존재합니다.");
        }
    }

    public List<TimeResponse> getAllTimes() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public void delete(final Long id) {
        List<Reservation> reservations = reservationRepository.findAllByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new BadRequestException("해당 시간에 예약된 내역이 존재하므로 삭제할 수 없습니다.");
        }
        final boolean deleted = timeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, final Long themeId) {
        return timeRepository.findAvailableTimes(date, themeId);
    }
}
