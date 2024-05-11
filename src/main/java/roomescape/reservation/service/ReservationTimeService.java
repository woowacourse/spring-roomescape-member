package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.dto.AvailableTimeResponse;
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
        if (reservationTimeRepository.existByStartAt(time)) {
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
        if (reservationRepository.existByTimeId(timeId)) {
            throw new IllegalArgumentException("예약이 존재해 시간을 삭제할 수 없습니다.");
        }

        if (!reservationTimeRepository.deleteById(timeId)) {
            throw new IllegalArgumentException(
                    String.format("잘못된 예약 시간입니다. id %d를 확인해주세요.", timeId));
        }
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, long themeId) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        Set<ReservationTime> reservedTimes = reservationTimeRepository.findReservedTime(date, themeId);
        return times.stream()
                .map(time -> new AvailableTimeResponse(
                        time.getId(),
                        time.getStartAt(),
                        reservedTimes.contains(time)
                ))
                .toList();
    }
}
