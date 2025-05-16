package roomescape.reservation.service;


import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.error.ConflictException;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public void remove(Long id) {
        validateReservationConstraint(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateReservationConstraint(Long id) {
        List<Reservation> constraintReservations = reservationRepository.findAllByTimeId(id);
        if (!constraintReservations.isEmpty()) {
            throw new ConflictException("해당 시간과 연관된 예약이 있어 삭제할 수 없습니다.");
        }
    }

    public ReservationTimeResponse add(ReservationTimeRequest request) {
        ReservationTime newTime = request.toTimeWithoutId();
        validateDuplicateTime(request);
        Long id = reservationTimeRepository.saveAndReturnId(request.toTimeWithoutId());
        return ReservationTimeResponse.from(newTime.withId(id));
    }

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();

    }

    private void validateDuplicateTime(ReservationTimeRequest request) {
        if (reservationTimeRepository.existSameStartAt(request.startAt())) {
            throw new ConflictException("해당 시간은 이미 존재합니다.");
        }
    }

}
