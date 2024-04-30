package roomescape.reservationtime.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public CreateReservationTimeResponse createReservationTime(final CreateReservationTimeRequest createReservationTimeRequest) {
        Long id = reservationTimeRepository.save(createReservationTimeRequest.toReservationTime());

        // TODO: 수정
        FindReservationTimeResponse findReservationTimeResponse = getReservationTime(id);
        return CreateReservationTimeResponse.of(findReservationTimeResponse);
    }

    public List<FindReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(FindReservationTimeResponse::of)
                .toList();
    }

    public FindReservationTimeResponse getReservationTime(final Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));
        return FindReservationTimeResponse.of(reservationTime);
    }

    public void deleteById(final Long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));
        reservationTimeRepository.deleteById(id);
    }
}
