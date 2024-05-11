package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.ReservationTimeCreationRequest;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse register(ReservationTimeCreationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.save(request.toReservationTime());
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(long id) {
        validateReservedTime(id);
        boolean isDeleted = reservationTimeRepository.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }

    private void validateReservedTime(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다");
        }
    }

    public List<AvailableTimeResponse> findAvailableTimes(long themeId, LocalDate date) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        Set<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(themeId, date);

        return reservationTimes.stream()
                .map(reservationTime -> {
                    boolean alreadyBooked = reservedTimeIds.contains(reservationTime.getId());
                    return AvailableTimeResponse.from(reservationTime, alreadyBooked);
                })
                .toList();
    }
}
