package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.AvailableTimeFindRequest;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.domain.ReservationTime;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private static final String TIME_NOT_FOUND = "요청한 시간을 찾을 수 없습니다.";
    private static final String TIME_HAS_RESERVATION = "해당 시간에 예약이 존재하여 삭제할 수 없습니다.";

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime create(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = ReservationTime.of(request.getStartAt());
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime find(long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new NotFoundException(TIME_NOT_FOUND));
    }

    public void delete(long reservationTimeId) {
        reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new NotFoundException(TIME_NOT_FOUND));
        if (reservationRepository.existsByTimeId(reservationTimeId)) {
            throw new ConflictException(TIME_HAS_RESERVATION);
        }
        reservationTimeRepository.delete(reservationTimeId);
    }

    public List<ReservationTime> findAvailable(AvailableTimeFindRequest request) {
        return reservationTimeRepository.findByDateAndTheme(request.getDate(), request.getThemeId());
    }
}
