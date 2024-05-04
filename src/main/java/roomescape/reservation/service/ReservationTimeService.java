package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.dto.TimeSaveRequest;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long save(TimeSaveRequest timeSaveRequest) {
        ReservationTime reservationTime = timeSaveRequest.toReservationTime();

        return reservationTimeRepository.save(reservationTime);
    }

    public TimeResponse findById(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다"));

        return TimeResponse.toResponse(reservationTime);
    }

    public List<TimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(TimeResponse::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationTimeRepository.findReservationInSameId(id).ifPresent(empty -> {
            throw new IllegalArgumentException("해당 시간으로 예약된 내역이 있습니다.");
        });
        reservationTimeRepository.delete(id);
    }
}
