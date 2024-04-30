package roomescape.reservation.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse create(ReservationTimeRequest reservationTimeRequest) {
        LocalTime time = LocalTime.parse(reservationTimeRequest.startAt());
        if (reservationTimeRepository.existsByStartAt(time)) {
            throw new IllegalArgumentException("시간이 중복되었습니다.");
        }

        ReservationTime reservationTime = new ReservationTime(time);
        return ReservationTimeResponse.from(reservationTimeRepository.save(reservationTime));
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(long timeId) {
        if (!reservationRepository.findAllByTimeId(timeId).isEmpty()) {
            throw new IllegalArgumentException("예약이 존재해 시간을 삭제할 수 없습니다.");
        }

        if (!reservationTimeRepository.deleteById(timeId)) {
            throw new IllegalArgumentException(
                    String.format("잘못된 예약 시간입니다. id %d를 확인해주세요.", timeId));
        }
    }
}
