package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.param.CreateReservationTimeParam;
import roomescape.service.result.ReservationTimeResult;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTImeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTImeRepository, final ReservationRepository reservationRepository) {
        this.reservationTImeRepository = reservationTImeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Long create(CreateReservationTimeParam createReservationTimeParam) {
        return reservationTImeRepository.create(new ReservationTime(createReservationTimeParam.startAt()));
    }

    public ReservationTimeResult findById(Long reservationTimeId) {
        ReservationTime reservationTime = reservationTImeRepository.findById(reservationTimeId).orElseThrow(
                () -> new IllegalArgumentException(reservationTimeId + "에 해당하는 reservation_time 튜플이 없습니다."));
        return toReservationResult(reservationTime);
    }

    public List<ReservationTimeResult> findAll() {
        List<ReservationTime> reservationTimes = reservationTImeRepository.findAll();
        return reservationTimes.stream()
                .map(this::toReservationResult)
                .toList();
    }

    public void deleteById(Long reservationTimeId) {
        if (reservationRepository.existByTimeId(reservationTimeId)) {
            throw new IllegalArgumentException("해당 예약 시간에 예약이 존재합니다.");
        }
        reservationTImeRepository.deleteById(reservationTimeId);
    }

    private ReservationTimeResult toReservationResult(ReservationTime reservationTime) {
        return new ReservationTimeResult(reservationTime.id(), reservationTime.startAt());
    }
}
