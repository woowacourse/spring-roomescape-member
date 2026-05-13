package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.dto.ReservationTimeCreateData;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeCreateRequest;
import roomescape.service.dto.response.ReservationTimeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeService::mapDomainToDto)
                .toList();
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest data) {
        final ReservationTime reservationTime = ReservationTime.create(
                new ReservationTimeCreateData(
                        data.startAt(),
                        data.endAt()
                )
        );

        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return mapDomainToDto(savedTime);
    }

    public void delete(final Long timeId) {
        final boolean deleted = reservationTimeRepository.delete(timeId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }

    private static ReservationTimeResponse mapDomainToDto(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
