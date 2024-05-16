package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeAvailability;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservation.ReservationTimeRepository;
import roomescape.service.dto.reservation.ReservationTimeSaveRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime createReservationTime(ReservationTimeSaveRequest request) {
        if (reservationTimeRepository.findByStartAt(request.startAt()).isPresent()) {
            throw new IllegalUserRequestException("이미 존재하는 예약 시간입니다.");
        }

        ReservationTime newReservationTime = ReservationTimeSaveRequest.toEntity(request);
        return reservationTimeRepository.save(newReservationTime);
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTimeAvailability> findReservationTimeAvailability(LocalDate date, Long themeId) {
        return reservationTimeRepository.findReservationTimeAvailabilities(date, themeId);
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new IllegalUserRequestException("이미 예약중인 시간은 삭제할 수 없습니다.");
        }

        int deletedCount = reservationTimeRepository.deleteById(id);
        if (deletedCount == 0) {
            throw new IllegalUserRequestException("존재하지 않는 예약 시간입니다.");
        }
    }
}
