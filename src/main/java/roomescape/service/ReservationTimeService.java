package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationTimeService {

    private final ReservationTimeRepository repository;

    @Transactional
    public ReservationTime create(
            ReservationTimeCreateCommand command
    ) {
        ReservationTime reservationTime = ReservationTime.create(command.startAt());

        return repository.persist(reservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAvailableTimes(
            long themeId,
            LocalDate date
    ) {
        return repository.findReservationAvailableTimes(themeId, date);
    }

    @Transactional
    public void delete(long timeId) {
        boolean deleted = repository.delete(timeId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 시간을 조회하지 못했습니다.",
                    "timeId = " + timeId
            );
        }
    }
}
