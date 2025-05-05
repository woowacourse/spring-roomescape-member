package roomescape.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.application.dto.response.ReservationTimeServiceResponse;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class UserReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationTimeServiceResponse> getAllWithStatus(Long themeId, LocalDate date) {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        List<ReservationTime> bookedTimes = reservationTimeRepository.getAllByThemeIdAndDate(themeId, date);

        return allTimes.stream()
                .map(time -> ReservationTimeServiceResponse.of(time, bookedTimes.contains(time)))
                .toList();
    }
}
