package roomescape.reservationtime.application.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.application.dao.AvailableTimeDao;
import roomescape.reservationtime.application.dto.AvailableReservationTimeResult;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.reservationtime.application.exception.ReservationTimeException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationTimeQueryService {

    private final ReservationTimeRepository timeRepository;
    private final AvailableTimeDao availableTimeDao;

    public ReservationTimeResult findById(Long timeId) {
        return ReservationTimeResult.from(timeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 시간 입니다.")));
    }

    public List<ReservationTimeResult> findAll() {
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public List<AvailableReservationTimeResult> findAvailableTimes(Long themeId, LocalDate date) {
        return availableTimeDao.findByThemeAndDate(themeId, date);
    }
}
