package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ReservationTimeConflictException;
import roomescape.global.exception.notFound.ReservationTimeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.service.dto.request.ReservationTimeRequest;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.time.service.dto.response.ReservationTimeWithBookedResponse;

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
        ReservationTime entity = requestDto.toEntity();
        validateOperatingTime(entity);
        validateDuplicated(entity);
        ReservationTime saved = timeRepository.save(entity);
        return ReservationTimeResponse.of(saved);
    }

    private void validateDuplicated(ReservationTime entity) {
        if (isExistDuplicatedWith(entity)) {
            throw new ReservationTimeConflictException();
        }
    }

    private boolean isExistDuplicatedWith(ReservationTime entity) {
        return timeRepository.findByStartAt(entity.getStartAt()).isPresent();
    }

    private void validateOperatingTime(ReservationTime newTime) {
        if (!newTime.isOnOperatingTime()) {
            throw new BadRequestException("운영 시간 이외의 시간이 입력되었습니다.");
        }
    }

    public List<ReservationTimeResponse> getAllTimes() {
        return timeRepository.findAll().stream()
                .map(ReservationTimeResponse::of)
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
                .map(ReservationTimeWithBookedResponse::of)
                .toList();
    }
}
