package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.error.ReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeResponse saveTime(final ReservationTimeRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(request.startAt()));
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void delete(final Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new ReservationException("해당 시간으로 예약된 건이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }
}
