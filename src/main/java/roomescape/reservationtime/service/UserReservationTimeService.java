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
    public List<AvailableTime> getSchedules(LocalDate date, long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        Set<Long> reservedIdSet = new HashSet<>(reservationRepository.findByDateAndTheme(date, themeId));

        return allTimes.stream()
                .map(time -> {
                    boolean notReserved = !reservedIdSet.contains(time.id());
                    boolean notPast = LocalDateTime.of(date, time.startAt()).isAfter(LocalDateTime.now());
                    return new AvailableTime(time.id(), time.startAt(), notReserved && notPast);
                })
                .toList();
    }
}