package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;
import org.springframework.stereotype.Service;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationSaveResponse save(ReservationSaveRequest body) {
        Reservation reservation = reservationRepository.save(body.name(), body.date(), body.timeId());

        return new ReservationSaveResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                new TimeInformation(reservation.getTime().getId(), reservation.getTime().getStartAt()));
    }

    public List<ReservationFindResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationFindResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        new TimeInformation(
                                reservation.getTime().getId(),
                                reservation.getTime().getStartAt()
                        )
                ))
                .toList();
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
