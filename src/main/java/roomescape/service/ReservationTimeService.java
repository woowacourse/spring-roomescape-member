package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationTimeServiceRequest;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeServiceResponse create(CreateReservationTimeServiceRequest command) {
        ReservationTime reservationTime = reservationTimeRepository.save(command.toReservationTime());
        return ReservationTimeServiceResponse.from(reservationTime);
    }

    public List<ReservationTimeServiceResponse> getAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.getAll();
        return reservationTimes.stream()
                .map(ReservationTimeServiceResponse::from)
                .toList();
    }

    public void delete(Long id) {
        // TODO: noContent vs IllegalArgumentException
        ReservationTime reservation = getById(id);
        reservationTimeRepository.remove(reservation);
    }

    private ReservationTime getById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시간이 존재하지 않습니다."));
    }
}
