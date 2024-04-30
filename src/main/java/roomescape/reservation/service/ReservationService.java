package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.RequestReservation;
import roomescape.reservation.dto.ResponseReservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ResponseTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long save(RequestReservation requestReservation) {
        ReservationTime reservationTime = reservationTimeRepository.findById(requestReservation.timeId());

        Reservation reservation = new Reservation(new Name(requestReservation.name()),
                requestReservation.date(),
                reservationTime);

        return reservationRepository.save(reservation);
    }

    public ResponseReservation findOneById(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        ResponseTime responseTime = new ResponseTime(reservation.getTime().getId(),
                reservation.getTime().getStartAt());

        return new ResponseReservation(reservation.getId(),
                reservation.getName(), reservation.getDate(), responseTime);
    }

    public List<ResponseReservation> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    ResponseTime responseTime = new ResponseTime(reservation.getTime().getId(),
                            reservation.getTime().getStartAt());
                    return new ResponseReservation(reservation.getId(),
                            reservation.getName(), reservation.getDate(), responseTime);
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
