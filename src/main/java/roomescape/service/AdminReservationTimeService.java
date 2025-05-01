package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationTimeServiceRequest;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@Service
@RequiredArgsConstructor
public class AdminReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeServiceResponse create(CreateReservationTimeServiceRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.save(request.toReservationTime());
        return ReservationTimeServiceResponse.withoutBook(reservationTime);
    }

    // TODO : User와 응답DTO 분리하기
    public List<ReservationTimeServiceResponse> getAll() {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        return allTimes.stream()
                .map(ReservationTimeServiceResponse::withoutBook)
                .toList();
    }

    public void delete(Long id) {
        // TODO: noContent vs IllegalArgumentException
        ReservationTime reservationTime = reservationTimeRepository.getById(id);
        reservationTimeRepository.remove(reservationTime);
    }
}
