package roomescape.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequestDto;
import roomescape.dto.response.ReservationTimeWithStateDto;
import roomescape.exception.BadRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime create(final ReservationTimeRequestDto request) {
        final ReservationTime time = new ReservationTime(request.getStartAt());
        validateDuplicatedStartAt(time);
        final Long id = reservationTimeRepository.save(time);
        return new ReservationTime(id, time.getStartAt());
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeWithStateDto> findAllWithReservationState(final String date, final long themeId) {
        final List<ReservationTime> times = reservationTimeRepository.findAll();
        final Set<Long> reservedTimeIds = reservationRepository.findAllByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getTimeId)
                .collect(toSet());
        return times.stream()
                .map(reservationTime -> toDto(reservationTime, reservedTimeIds))
                .toList();
    }

    @Transactional
    public void delete(final long id) {
        if (reservationRepository.hasReservationAtTime(id)) {
            throw new BadRequestException("해당 시간에 예약한 내역이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }

    private void validateDuplicatedStartAt(final ReservationTime reservationTime) {
        final String startAt = reservationTime.getStartAtString();
        if (reservationTimeRepository.hasDuplicateReservationTime(startAt)) {
            throw new BadRequestException("해당 시간이 이미 존재합니다.");
        }
    }

    private ReservationTimeWithStateDto toDto(final ReservationTime time, final Set<Long> reservedTimeIds) {
        final boolean alreadyBooked = reservedTimeIds.contains(time.getId());
        return new ReservationTimeWithStateDto(time.getId(), time.getStartAtString(), alreadyBooked);
    }
}
