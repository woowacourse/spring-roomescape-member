package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DeleteReservationException;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime saveReservationTime(ReservationTimeRequest request) {
        return reservationTimeRepository.saveReservationTime(request.toReservationTime());
    }

    public List<ReservationTime> readReservationTime() {
        return reservationTimeRepository.readReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        boolean existsByTimeId = reservationRepository.existsByTimeId(id);
        if (existsByTimeId) {
            throw new DeleteReservationException("해당 시간을 사용하는 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }

        reservationTimeRepository.deleteReservationTime(id);
    }

    public List<ReservationTimeResponseWithBookedStatus> readAvailableTimesBy(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.readReservationTimes();
        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimesBy(date, themeId);

        return allTimes.stream()
                .map(time ->
                        ReservationTimeResponseWithBookedStatus.of(time, !availableTimes.contains(time))
                ).toList();
    }
}
