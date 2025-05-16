package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.error.ReservationException;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeResponse saveTime(final ReservationTimeRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(request.startAt()));
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllAvailable(final LocalDate date, final Long themeId) {
        return reservationTimeRepository.findAllAvailable(date, themeId);
    }

    public void delete(final Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new ReservationException("해당 시간으로 예약된 건이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }
}
