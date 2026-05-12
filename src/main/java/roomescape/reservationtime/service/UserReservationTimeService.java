package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

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
    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<AvailableTime> getSchedules(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();

        List<Long> reservedTimeIds = reservationRepository.findByDateAndTheme(date, themeId);
        Set<Long> reservedIdSet = new HashSet<>(reservedTimeIds);

        return allTimes.stream()
                .map(time -> new AvailableTime(
                        time.id(),
                        time.startAt(),
                        !reservedIdSet.contains(time.id()) && LocalDateTime.of(date, time.startAt())
                                .isAfter(LocalDateTime.now())
                ))
                .toList();
    }
}
