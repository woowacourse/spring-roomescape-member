package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.command.CreateReservationCommand;
import roomescape.service.dto.query.ReservationQuery;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationQuery create(CreateReservationCommand command) {
        ReservationTime reservationTime = getReservationTimeById(command.timeId());
        Reservation reservation = command.toReservation(reservationTime);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationQuery.from(savedReservation);
    }

    public List<ReservationQuery> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationQuery::from)
                .toList();
    }

    public void delete(Long id) {
        Reservation target = getById(id);
        reservationRepository.remove(target);
    }

    private ReservationTime getReservationTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시간이 존재하지 않습니다."));
    }

    private Reservation getById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 예약이 존재하지 않습니다."));
    }
}
