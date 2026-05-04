package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.dto.ReservationCreateCommand;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(final ReservationCreateRequest data) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(data.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        final Reservation reservation = Reservation.create(
                new ReservationCreateCommand(
                        data.name(),
                        data.date(),
                        reservationTime
                )
        );

        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    public void delete(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }
}
