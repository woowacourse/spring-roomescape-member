package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.web.dto.ReservationTimeRequest;
import roomescape.web.dto.ReservationTimeResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ReservationTimeResponse register(ReservationTimeRequest request) {
        LocalTime startAt = request.startAt();

        validateDuplicateTime(startAt);
        ReservationTime reservationTime = new ReservationTime(startAt);

        return ReservationTimeResponse.from(reservationTimeRepository.save(reservationTime));
    }

    @Transactional
    public void remove(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    private void validateDuplicateTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicateEntityException("이미 등록된 예약 시간 입니다. %s", startAt);
        }
    }
}
