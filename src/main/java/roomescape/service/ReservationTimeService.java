package roomescape.service;

import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION_TIME;
import static roomescape.exception.ExceptionType.INVALID_DELETE_TIME;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.existsByStartAt(reservationTimeRequest.startAt())) {
            throw new RoomescapeException(DUPLICATE_RESERVATION_TIME);
        }
        ReservationTime reservationTime = new ReservationTime(reservationTimeRequest.startAt());
        ReservationTime saved = reservationTimeRepository.save(reservationTime);
        return toResponse(saved);
    }

    private ReservationTimeResponse toResponse(ReservationTime saved) {
        return new ReservationTimeResponse(saved.getId(), saved.getStartAt());
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    //TODO : 테스트 생성
    public void delete(long id) {
        //todo SQL로 구현
        List<Reservation> reservations = reservationRepository.findAll();
        //TODO : 지역변수 네이밍 고민
        boolean invalidDelete = reservations.stream()
                .anyMatch(reservation -> reservation.isReservationTimeOf(id));
        if (invalidDelete) {
            throw new RoomescapeException(INVALID_DELETE_TIME);
        }
        reservationTimeRepository.delete(id);
    }
}
