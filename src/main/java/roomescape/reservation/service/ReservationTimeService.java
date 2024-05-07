package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.AvailableTimeResponse;
import roomescape.reservation.dto.TimeCreateRequest;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long save(TimeCreateRequest timeCreateRequest) {
        ReservationTime reservationTime = timeCreateRequest.toReservationTime();
        return reservationTimeRepository.save(reservationTime);
    }

    public TimeResponse findById(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다"));

        return TimeResponse.toResponse(reservationTime);
    }

    public boolean isExist(Long id) {
        Optional<ReservationTime> time = reservationTimeRepository.findById(id);
        if (time.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<TimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(TimeResponse::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (reservationTimeRepository.findReservationInSameId(id).isPresent()) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 있습니다.");
        }
        reservationTimeRepository.delete(id);
    }

    public List<AvailableTimeResponse> findAvailableTime(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableTime(date, themeId);
    }
}
