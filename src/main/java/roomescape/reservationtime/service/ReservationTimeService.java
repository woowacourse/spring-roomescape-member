package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeException;
import roomescape.reservationtime.repository.AvailableReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeException;

@RequiredArgsConstructor
@Service
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;

    public ReservationTimeResponse findById(Long timeId) {
        return ReservationTimeResponse.from(timeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException("[ERROR] 존재하지 않는 시간 입니다.")));
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> times = timeRepository.findAll();

        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<AvailableReservationTime> times = timeRepository.findByThemeAndDate(themeId, date);

        return times.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeCreateRequest request) {
        validateDuplicateTime(request.startAt());
        ReservationTime time = request.toEntity();

        ReservationTime savedTime = timeRepository.save(time);

        return ReservationTimeResponse.from(savedTime);
    }

    public int delete(Long id) {
        return timeRepository.delete(id);
    }

    private void validateDuplicateTime(LocalTime startAt) {
        if (timeRepository.existsByStartAt(startAt)) {
            throw new ReservationTimeException(String.format("[ERROR] 시간 %s이(가) 이미 존재합니다.",
                    startAt.format(DateTimeFormatter.ofPattern("HH:mm"))));
        }
    }
}
