package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.exception.badRequest.BadRequestException;
import roomescape.exception.conflict.ReservationTimeConflictException;
import roomescape.exception.notFound.ReservationTimeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.service.dto.request.ReservationTimeRequest;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.time.service.dto.response.ReservationTimeWithBookedResponse;

import java.time.LocalDate;
import java.util.List;

// TODO: findByXXX - DataAccessException 핸들링
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
        ReservationTime entity = requestDto.toEntity();
        validateDuplicated(entity);
        ReservationTime saved = timeRepository.save(entity);
        return ReservationTimeResponse.from(saved);
    }

    private void validateDuplicated(ReservationTime entity) {
        if (isExistDuplicatedWith(entity)) {
            throw new ReservationTimeConflictException();
        }
    }

    private boolean isExistDuplicatedWith(ReservationTime entity) {
        return timeRepository.findByStartAt(entity.getStartAt()).isPresent();
    }

    public List<ReservationTimeResponse> getAllTimes() {
        return timeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(final Long id) {
        List<Reservation> reservations = reservationRepository.findAllByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new BadRequestException("해당 시간에 예약된 내역이 존재하므로 삭제할 수 없습니다.");
        }
        final boolean deleted = timeRepository.deleteById(id);
        if (!deleted) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

    public List<ReservationTimeWithBookedResponse> getAllTimesWithBooked(LocalDate date, final Long themeId) {
        return timeRepository.findAllWithBooked(date, themeId)
                .stream()
                .map(ReservationTimeWithBookedResponse::from)
                .toList();
    }
}
