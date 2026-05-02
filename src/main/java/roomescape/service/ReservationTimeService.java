package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.dto.ReservationTimeCreateCommand;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeData;
import roomescape.service.dto.response.ReservationTimeResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationTimeResult> getTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public ReservationTimeResult create(ReservationTimeData data) {
        final ReservationTime reservationTime = ReservationTime.create(
                new ReservationTimeCreateCommand(
                        data.startAt()
                )
        );

        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResult.from(savedTime);
    }

    public void delete(final Long timeId) {
        final boolean deleted = reservationTimeRepository.delete(timeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }
}
