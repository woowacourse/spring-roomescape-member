package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.TimeResponse;
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

        if (LocalDate.now().isBefore(reservationRequest.date())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }

        Reservation reservation = new Reservation(new Name(reservationRequest.name()),
                reservationRequest.date(),
                reservationTime);

        if (reservationRepository.existReservation(reservation)) {
            throw new IllegalArgumentException("중복된 예약이 있습니다.");
        }

        return reservationRepository.save(reservation);
    }

    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        TimeResponse timeResponse = new TimeResponse(reservation.getTime().getId(),
                reservation.getTime().getStartAt());

        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), timeResponse);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    TimeResponse timeResponse = new TimeResponse(reservation.getTime().getId(),
                            reservation.getTime().getStartAt());
                    return new ReservationResponse(reservation.getId(),
                            reservation.getName(), reservation.getDate(), timeResponse);
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
