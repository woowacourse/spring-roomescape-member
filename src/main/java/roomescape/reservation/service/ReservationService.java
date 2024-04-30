package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
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

    public Long save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        Reservation reservation = new Reservation(new Name(reservationRequest.name()),
                reservationRequest.date(),
                reservationTime);

        return reservationRepository.save(reservation);
    }

    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        ResponseTime responseTime = new ResponseTime(reservation.getTime().getId(),
                reservation.getTime().getStartAt());

        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), responseTime);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    ResponseTime responseTime = new ResponseTime(reservation.getTime().getId(),
                            reservation.getTime().getStartAt());
                    return new ReservationResponse(reservation.getId(),
                            reservation.getName(), reservation.getDate(), responseTime);
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
