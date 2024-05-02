package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.AvailableTimeResponse;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimesRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimesRepository,
                                  ReservationQueryRepository reservationQueryRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimesRepository = reservationTimesRepository;
        this.reservationQueryRepository = reservationQueryRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTimeRequest request) {
        if (existsByStartAt(request.startAt())) {
            throw new IllegalStateException(String.format("이미 존재하는 예약시간이 있습니다. 해당 시간:%s", request.startAt()));
        }
        ReservationTime reservationTime = reservationTimesRepository.create(request.toReservationTime());
        return ReservationTimeResponse.from(reservationTime);
    }

    private boolean existsByStartAt(LocalTime startAt) {
        return reservationTimesRepository.existsByStartAt(startAt);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimesRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        ReservationTime findReservationTime = reservationTimesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));
        long reservedCount = reservationRepository.findReservationCountByTimeId(id);
        if (reservedCount > 0) {
            throw new IllegalStateException(String.format("해당 예약 시간에 연관된 예약이 존재하여 삭제할 수 없습니다. 삭제 요청한 시간:%s",
                    findReservationTime.getStartAt()));
        }
        reservationTimesRepository.deleteById(id);
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, long themeId) {
        return reservationQueryRepository.findAvailableReservationTimes(date, themeId)
                .stream()
                .map(AvailableTimeResponse::from)
                .toList();
    }
}
