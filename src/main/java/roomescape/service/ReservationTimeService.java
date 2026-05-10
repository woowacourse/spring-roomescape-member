package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.DataReferencedException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationTime create(
            ReservationTimeCreateCommand command
    ) {
        UUID id = UUID.randomUUID();
        ReservationTime reservationTime = new ReservationTime(id, command.startAt());

        return timeRepository.persist(reservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return timeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAvailableTimes(
            UUID themeId,
            LocalDate date
    ) {
        List<ReservationTime> allTimes = timeRepository.findAll();
        List<Reservation> existReservations = reservationRepository.findByDateAndThemeId(date, themeId);

        Set<UUID> inUsedTimeIds = existReservations.stream()
                .map(Reservation::timeId)
                .collect(Collectors.toUnmodifiableSet());

        return allTimes.stream()
                .filter(time -> !inUsedTimeIds.contains(time.id()))
                .toList();
    }

    @Transactional
    public void delete(UUID timeId) {
        try {
            boolean deleted = timeRepository.delete(timeId);
            validateDeleted(deleted, timeId);
        } catch (DataReferencedException exception) {
            throw new InUseEntityException(
                    "사용 중인 예약 시간은 제거할 수 없습니다.",
                    "timeId = " + timeId,
                    exception
            );
        }
    }

    private void validateDeleted(boolean deleted, UUID timeId) {
        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 시간을 조회하지 못했습니다.",
                    "timeId = " + timeId
            );
        }
    }
}
