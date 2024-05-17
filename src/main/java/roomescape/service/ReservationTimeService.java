package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.exceptions.DeleteException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime save(ReservationTimeRequest reservationTimeRequest) {
        validateTime(reservationTimeRequest);
        ReservationTime reservationTime = new ReservationTime(reservationTimeRequest.startAt());
        return reservationTimeRepository.insert(reservationTime);
    }

    private void validateTime(ReservationTimeRequest reservationTimeRequest) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.selectAll();
        reservationTimes.forEach(reservationTime -> reservationTime.validateNotDuplicated(reservationTimeRequest.startAt()));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.selectAll();
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.selectAll();
        List<Long> reservedTimeIds = reservationRepository.findReservedTimeIds(date, themeId);

        return reservationTimes.stream()
                .map(reservationTime -> AvailableTimeResponse.from(reservationTime, isReservedTime(reservationTime, reservedTimeIds)))
                .toList();
    }

    private boolean isReservedTime(ReservationTime reservationTime, List<Long> reservedTimeIds) {
        return reservedTimeIds.contains(reservationTime.getId());
    }

    public void delete(Long id) {
        if (reservationRepository.hasReservationTime(id)) {
            throw new DeleteException("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
