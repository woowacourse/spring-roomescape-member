package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.dto.ReservationTimeCreateCommand;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeCreateRequest;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeStatusResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeStatusResponse> getTimesWithStatus(final LocalDate date, final Long themeId) {
        return reservationTimeRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(ReservationTimeStatusResponse::from)
                .toList();
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest data) {
        final ReservationTime reservationTime = ReservationTime.create(
                new ReservationTimeCreateCommand(
                        data.startAt(),
                        data.endAt()
                )
        );

        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedTime);
    }

    public void delete(final Long timeId) {
        final boolean deleted = reservationTimeRepository.delete(timeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }
}
