package roomescape.reservationtime.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcReservationRepository reservationRepository;

    public ReservationTimeService(
            final JdbcReservationTimeRepository reservationTimeRepository,
            final JdbcReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public CreateReservationTimeResponse createReservationTime(
            final CreateReservationTimeRequest createReservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRepository.save(
                createReservationTimeRequest.toReservationTime());
        return CreateReservationTimeResponse.of(reservationTime);
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
        List<Reservation> reservations = reservationRepository.findAllByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("시간을 사용 중인 예약이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }
}
