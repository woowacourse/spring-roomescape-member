package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.DataExistException;
import roomescape.exception.DataNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public Long save(final LocalTime startAt) {
        final List<ReservationTime> foundReservationTimes = reservationTimeRepository.findAllByStartAt(startAt);
        if (!foundReservationTimes.isEmpty()) {
            throw new DataExistException("해당 예약 시간이 이미 존재합니다. startAt = " + startAt);
        }

        final ReservationTimeEntity reservationTimeEntity = new ReservationTimeEntity(startAt);

        return reservationTimeRepository.save(reservationTimeEntity);
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

        Long count = reservationRepository.countByTimeId(id);
        if (count >= 1) {
            throw new DataExistException("해당 예약 시간을 사용하고 있는 예약 정보가 존재합니다. id = " + id);
        }

        reservationTimeRepository.deleteById(id);
    }
}
