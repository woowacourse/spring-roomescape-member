package roomescape.time.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.policy.ReservationTimeDeletionNotAllowedException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.controller.dto.request.CreateReservationTimeRequest;
import roomescape.time.controller.dto.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationTimeResponse addReservationTime(CreateReservationTimeRequest request) {
        CreateReservationTimeParams params = new CreateReservationTimeParams(request.startAt());
        ReservationTime savedReservationTime = reservationTimeRepository.save(params);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void removeRegisteredReservationTime(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);

        validateNotReservedInFuture(reservationTime);

        reservationTimeRepository.deleteById(id);
    }

    private void validateNotReservedInFuture(ReservationTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();
        List<ReservationTime> reservedTimes = reservationRepository.findReservationsFrom(now.toLocalDate())
                .stream()
                .filter(reservation -> reservation.isFutureOrPresent(now))
                .map(Reservation::getTime)
                .toList();

        if(reservedTimes.contains(reservationTime)) {
            throw new ReservationTimeDeletionNotAllowedException();
        }
    }
}
