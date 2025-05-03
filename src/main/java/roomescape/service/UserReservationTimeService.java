package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationAvailableTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class UserReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationTimeService(ReservationRepository reservationRepository,
                                      ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(LocalDate date, Long themeId) {
        List<Long> bookedTimeIds = reservationRepository.findBookedTimeIdsByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(time -> ReservationAvailableTimeResponse.of(time, bookedTimeIds.contains(time.id())))
                .toList();
    }
}
