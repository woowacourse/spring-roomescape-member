package roomescape.service;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.web.dto.ReservationTimeRequest;
import roomescape.web.dto.ReservationTimeResponse;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ReservationTimeResponse register(ReservationTimeRequest request) {
        requireNonNull(request, "예약 시간 정보가 필요합니다.");

        validateAlreadyTime(request.startAt());
        ReservationTime reservationTime = new ReservationTime(request.startAt());

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

    private void validateAlreadyTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicateEntityException("이미 등록된 예약 시간 입니다. %s", startAt);
        }
    }
}
