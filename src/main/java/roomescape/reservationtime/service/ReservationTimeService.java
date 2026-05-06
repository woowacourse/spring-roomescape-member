package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@RequiredArgsConstructor
@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTime findById(Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new ReservationTimeException("[ERROR] 존재하지 않는 시간 입니다."));
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> result = reservationTimeRepository.findAll();

        return result.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findByThemeAndDate(themeId, date);

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = request.toEntity();

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public int delete(Long id) {
        return reservationTimeRepository.delete(id);
    }
}
