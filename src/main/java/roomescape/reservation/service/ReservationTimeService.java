package roomescape.reservation.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public List<ReservationTimeResponse> getAllTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public long createTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }

        return reservationTimeRepository.save(ReservationTime.of(startAt));
    }

    public void deleteTime(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("예약에 사용 중인 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.delete(id);
    }

    public ReservationTime getTime(long reservationId) {
        Optional<ReservationTime> findTime = reservationTimeRepository.findById(reservationId);

        if (findTime.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }

        return findTime.get();
    }
}
