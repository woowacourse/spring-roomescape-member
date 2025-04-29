package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.DataExistException;
import roomescape.exception.DataNotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public Long save(final LocalTime startAt) {
        final List<ReservationTime> foundReservationTimes = reservationTimeRepository.findAllByStartAt(startAt);
        if (!foundReservationTimes.isEmpty()) {
            throw new DataExistException("해당 예약 시간이 이미 존재합니다. startAt = " + startAt);
        }

        final ReservationTime reservationTime = new ReservationTime(startAt);

        return reservationTimeRepository.save(reservationTime);
    }

    public ReservationTime getById(final Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + id));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public void deleteById(final Long id) {
        final Optional<ReservationTime> found = reservationTimeRepository.findById(id);

        if (found.isEmpty()) {
            throw new DataNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + id);
        }
        reservationTimeRepository.delete(found.get());
    }
}
