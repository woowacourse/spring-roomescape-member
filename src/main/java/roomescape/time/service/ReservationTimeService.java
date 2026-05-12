package roomescape.time.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.service.dto.AvailableTimesResult;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.DuplicateTimeException;
import roomescape.time.exception.TimeNotFoundException;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTime registerReservationTime(ReservationTimeCommand command) {
        if (reservationTimeRepository.existByStartAt(command.startAt())) {
            throw new DuplicateTimeException();
        }

        ReservationTime reservationTime = ReservationTime.of(command.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void removeReservationTimeById(Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new TimeNotFoundException();
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public AvailableTimesResult findAvailableReservationTimes(Long themeId, LocalDate date) {
        return new AvailableTimesResult(reservationTimeRepository.findAvailableTimes(themeId, date));
    }
}
