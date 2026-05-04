package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.dto.ReservationTimeCreateCommand;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.dto.request.ReservationTimeCreateRequest;
import roomescape.service.dto.response.ReservationTimeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest data) {
        final ReservationTime reservationTime = ReservationTime.create(
                new ReservationTimeCreateCommand(
                        data.startAt()
                )
        );

        final ReservationTime savedTime = reservationTimeDao.save(reservationTime);

        return ReservationTimeResponse.from(savedTime);
    }

    public void delete(final Long timeId) {
        final boolean deleted = reservationTimeDao.delete(timeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }
}
