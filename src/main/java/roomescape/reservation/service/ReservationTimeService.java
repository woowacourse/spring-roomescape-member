package roomescape.reservation.service;

import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_TIME_ALEADY_EXISTS;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_TIME_ALREADY_USED;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_TIME_NOT_FOUND;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.exception.ReservationTimeCreateException;
import roomescape.reservation.service.exception.ReservationTimeDeleteException;
import roomescape.reservation.service.exception.ReservationTimeNotFoundException;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public List<ReservationTimeResponse> getAllTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime createTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ReservationTimeCreateException(RESERVATION_TIME_ALEADY_EXISTS);
        }

        return reservationTimeRepository.save(ReservationTime.of(startAt));
    }

    public ReservationTime getTime(long reservationId) {
        Optional<ReservationTime> findTime = reservationTimeRepository.findById(reservationId);

        if (findTime.isEmpty()) {
            throw new ReservationTimeNotFoundException(RESERVATION_TIME_NOT_FOUND);
        }

        return findTime.get();
    }

    public void deleteReservationTime(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new ReservationTimeDeleteException(RESERVATION_TIME_ALREADY_USED);
        }

        reservationTimeRepository.delete(id);
    }
}
