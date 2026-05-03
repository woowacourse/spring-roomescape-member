package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.command.ReservationTimeCreateCommand;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository repository;

    public ReservationTimeService(
            ReservationTimeRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public ReservationTime create(
            ReservationTimeCreateCommand createCommand
    ) {
        ReservationTime reservationTime = ReservationTime.create(createCommand.startAt());

        return repository.persist(reservationTime);
    }

    @Transactional
    public List<ReservationTime> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(long timeId) {
        boolean deleted = repository.delete(timeId);

        if (!deleted) {
            throw new EntityNotFoundException("삭제할 시간을 조회하지 못했습니다. timeId = " + timeId);
        }
    }
}
