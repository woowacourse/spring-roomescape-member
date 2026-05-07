package roomescape.reservationtime.application.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.application.dto.AvailableReservationTimeQueryResult;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.reservationtime.application.exception.ReservationTimeException;
import roomescape.reservationtime.domain.repository.AvailableReservationTime;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;

@RequiredArgsConstructor
@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeQueryResult findById(Long timeId) {
        return ReservationTimeQueryResult.from(timeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 시간 입니다.")));
    }

    public List<ReservationTimeQueryResult> findAll() {
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
                .map(ReservationTimeQueryResult::from)
                .toList();
    }

    public List<AvailableReservationTimeQueryResult> findAvailableTimes(Long themeId, LocalDate date) {
        List<AvailableReservationTime> times = timeRepository.findByThemeAndDate(themeId, date);

        return times.stream()
                .map(AvailableReservationTimeQueryResult::from)
                .toList();
    }

    public ReservationTimeQueryResult save(ReservationTimeCreateCommand request) {
        validateDuplicateTime(request.startAt());
        ReservationTime time = request.toEntity();

        ReservationTime savedTime = timeRepository.save(time);

        return ReservationTimeQueryResult.from(savedTime);
    }

    public int delete(Long id) {
        return timeRepository.delete(id);
    }

    private void validateDuplicateTime(LocalTime startAt) {
        if (timeRepository.existsByStartAt(startAt)) {
            throw new ReservationTimeException(String.format("시간 %s이(가) 이미 존재합니다.",
                    startAt.format(DateTimeFormatter.ofPattern("HH:mm"))));
        }
    }
}
