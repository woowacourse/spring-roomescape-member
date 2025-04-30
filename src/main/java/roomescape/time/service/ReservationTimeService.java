package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.dto.AvailableReservationTimeResponse;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.entity.ReservationTimeEntity;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository timeRepository,
            ReservationRepository reservationRepository
    ) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(ReservationTimeRequest requestDto) {
        ReservationTimeEntity entity = requestDto.toEntity();
        validateOperatingTime(entity);
        validateDuplicated(entity);
        ReservationTimeEntity saved = timeRepository.save(entity);
        return ReservationTimeResponse.from(saved);
    }

    private void validateOperatingTime(ReservationTimeEntity entity) {
        if (!entity.isAvailable()) {
            throw new BadRequestException("운영 시간 이외의 날짜는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicated(ReservationTimeEntity entity) {
        List<ReservationTimeEntity> times = timeRepository.findAll();
        if (times.stream().anyMatch(time -> time.isDuplicatedWith(entity))) {
            throw new ConflictException("러닝 타임이 겹치는 시간이 존재합니다.");
        }
    }

    public List<ReservationTimeResponse> getAllTimes() {
        return timeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(final Long id) {
        List<ReservationEntity> reservations = reservationRepository.findAllByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new BadRequestException("해당 시간에 예약된 내역이 존재하므로 삭제할 수 없습니다.");
        }
        final boolean deleted = timeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    public List<AvailableReservationTimeResponse> getAvailableTimes(LocalDate date, final Long themeId) {
        return timeRepository.findAvailableTimes(date, themeId);
    }
}
