package roomescape.reservationtime;

import java.util.HashSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public UserReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                      ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }


    @Transactional(readOnly = true)
    public ScheduleResponse getSchedules(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();

        List<Long> reservedTimeIds = reservationRepository.findReservedTimeIds(date, themeId);
        Set<Long> reservedIdSet = new HashSet<>(reservedTimeIds);

        List<AvailableTime> availableTimes = allTimes.stream()
                .map(time -> new AvailableTime(
                        time.id(),
                        time.startAt(),
                        !reservedIdSet.contains(time.id())
                ))
                .collect(Collectors.toList());

        return new ScheduleResponse(themeId, date, availableTimes);
    }
}
