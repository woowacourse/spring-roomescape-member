package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.UserName;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationCreateRequest reservationCreateRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(reservationCreateRequest.timeId());
        Reservation reservation = new Reservation(
                new UserName(reservationCreateRequest.name()),
                reservationCreateRequest.date(),
                reservationTime
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public void delete(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new NoSuchElementException("삭제할 예약이 존재하지 않습니다");
        }
    }
}
