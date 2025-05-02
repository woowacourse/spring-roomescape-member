package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.error.exception.BadRequestException;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createTime(ReservationTimeRequest request) {
        ReservationTime time = request.toEntity();
        validateOperatingTime(time);
        validateDuplicated(time);
        ReservationTime saved = reservationTimeRepository.save(time);
        return ReservationTimeResponse.from(saved);
    }

    public List<ReservationTimeResponse> getAllTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> getAvailableTimes(LocalDate date, final Long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }

    public void deleteTime(final Long id) {
        List<Reservation> reservations = reservationRepository.findAllByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new BadRequestException("해당 시간에 예약된 내역이 존재하므로 삭제할 수 없습니다.");
        }
        final boolean deleted = reservationTimeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    private void validateOperatingTime(ReservationTime entity) {
        if (!entity.isAvailable()) {
            throw new BadRequestException("운영 시간 이외의 날짜는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicated(ReservationTime entity) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        if (times.stream().anyMatch(time -> time.isDuplicatedWith(entity))) {
            throw new ConflictException("러닝 타임이 겹치는 시간이 존재합니다.");
        }
    }
}
