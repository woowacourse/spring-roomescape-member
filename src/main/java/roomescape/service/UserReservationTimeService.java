package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@Service
@RequiredArgsConstructor
public class UserReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationTimeServiceResponse> getAllByThemeIdAndDate(Long themeId, LocalDate date) {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        List<ReservationTime> bookedTimes = reservationTimeRepository.getAllByThemeIdAndDate(themeId, date);

        return allTimes.stream()
                .map(time -> ReservationTimeServiceResponse.of(time, bookedTimes.contains(time)))
                .toList();
    }
}
