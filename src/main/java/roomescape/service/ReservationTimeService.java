package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.mapper.ReservationTimeResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeResponseMapper reservationTimeResponseMapper;

    @Transactional
    public ReservationTimeResponse create(
            ReservationTimeCreateCommand command
    ) {
        EntityId id = EntityId.random();
        ReservationTime reservationTime = new ReservationTime(id, command.startAt());

        ReservationTime persisted = timeRepository.persist(reservationTime);
        return reservationTimeResponseMapper.map(persisted);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> findAll() {
        return timeRepository.findAll()
                .stream()
                .map(reservationTimeResponseMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> findAvailableTimes(
            EntityId themeId,
            LocalDate date
    ) {
        List<Reservation> existReservations = reservationRepository.findByDateAndThemeId(date, themeId);
        Set<EntityId> usedTimeIds = existReservations.stream()
                .map(Reservation::timeId)
                .collect(Collectors.toUnmodifiableSet());

        List<ReservationTime> allTimes = timeRepository.findAll();
        LocalDateTime current = LocalDateTime.now();

        return allTimes.stream()
                .filter(time -> isReservationAvailableTime(time, date, current))
                .filter(time -> isNotUsedTime(time, usedTimeIds))
                .map(reservationTimeResponseMapper::map)
                .toList();
    }

    @Transactional
    public void delete(EntityId timeId) {
        validateTimeNotUsed(timeId);

        boolean deleted = timeRepository.delete(timeId);
        validateDeleted(deleted, timeId);
    }

    private boolean isReservationAvailableTime(
            ReservationTime time,
            LocalDate dateForReservation,
            LocalDateTime current
    ) {
        ReservationDateTime reservationDateTime = new ReservationDateTime(dateForReservation, time.startAt());

        return reservationDateTime.isAvailable(current);
    }

    private boolean isNotUsedTime(
            ReservationTime time,
            Set<EntityId> usedTimeIds
    ) {
        return !usedTimeIds.contains(time.id());
    }

    private void validateTimeNotUsed(EntityId timeId) {
        if (reservationRepository.existByTimeId(timeId)) {
            throw new IllegalStateException("사용되지 않는 시간만 제거할 수 있습니다. timeId = " + timeId.getValueAsString());
        }
    }

    private void validateDeleted(boolean deleted, EntityId timeId) {
        if (!deleted) {
            throw new EntityNotFoundException("삭제할 시간을 조회하지 못했습니다. timeId = " + timeId);
        }
    }
}
