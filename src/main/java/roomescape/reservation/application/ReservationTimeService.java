package roomescape.reservation.application;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.exception.AlreadyExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.ui.dto.CreateReservationTimeRequest;
import roomescape.reservation.ui.dto.CreateReservationTimeResponse;
import roomescape.reservation.ui.dto.ReservationTimeResponse;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public CreateReservationTimeResponse create(final CreateReservationTimeRequest request) {
        final LocalTime startAt = request.startAt();

        final List<ReservationTime> foundReservationTimes = reservationTimeRepository.findAllByStartAt(startAt);
        if (!foundReservationTimes.isEmpty()) {
            throw new AlreadyExistException("해당 예약 시간이 이미 존재합니다. startAt = " + startAt);
        }

        final ReservationTime reservationTimeEntity = new ReservationTime(startAt);
        final Long id = reservationTimeRepository.save(reservationTimeEntity);

        final ReservationTime found = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + id));

        return CreateReservationTimeResponse.from(found);
    }

    public void delete(final Long id) {
        final Optional<ReservationTime> found = reservationTimeRepository.findById(id);

        if (found.isEmpty()) {
            throw new ResourceNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + id);
        }

        try {
            reservationTimeRepository.deleteById(id);
        } catch (final DataIntegrityViolationException e) {
            throw new AlreadyExistException("해당 예약 시간을 사용하고 있는 예약 정보가 존재합니다. id = " + id);
        }
    }

    public List<ReservationTimeResponse> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
