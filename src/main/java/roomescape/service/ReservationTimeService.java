package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.TimeWithStatusResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationtimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationtimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationtimeRepository = reservationtimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationtimeRepository.findAll();
    }

    public ReservationTime getReservationTime(Long id) {
        return reservationtimeRepository.getById(id);
    }

    public List<TimeWithStatusResponse> getReservationTimesWithAvailability(LocalDate date, Long themeId) {
        List<ReservationTime> times = getReservationTimes();
        Set<Long> reservedTimeIds = reservationRepository.findReservedTimeIdsByDateAndThemeId(date, themeId);

        return times.stream()
                .map(time -> TimeWithStatusResponse.from(time, reservedTimeIds.contains(time.getId())))
                .toList();
    }

    @Transactional
    public ReservationTime addReservationTime(ReservationTimeRequest request) {
        Long id = reservationtimeRepository.save(new ReservationTime(request.startAt()));
        return getReservationTime(id);
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용 중인 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationtimeRepository.deleteById(id);
    }
}
