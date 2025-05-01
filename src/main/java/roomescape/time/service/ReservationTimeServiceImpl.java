package roomescape.time.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.time.service.converter.ReservationTimeConverter;
import roomescape.time.service.dto.CreateReservationTimeServiceRequest;
import roomescape.time.service.usecase.ReservationTimeCommandUseCase;
import roomescape.time.service.usecase.ReservationTimeQueryUseCase;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.controller.dto.CreateReservationTimeWebRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeServiceImpl implements ReservationTimeService {

    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;
    private final ReservationTimeCommandUseCase reservationTimeCommandUseCase;

    @Override
    public List<ReservationTimeResponse> getAll() {
        return ReservationTimeConverter.toDto(
                reservationTimeQueryUseCase.getAll());
    }

    @Override
    public ReservationTimeResponse create(final CreateReservationTimeWebRequest createReservationTimeWebRequest) {
        return ReservationTimeConverter.toDto(
                reservationTimeCommandUseCase.create(
                        new CreateReservationTimeServiceRequest(
                                createReservationTimeWebRequest.startAt())));
    }

    @Override
    public void delete(final Long id) {
        reservationTimeCommandUseCase.delete(ReservationTimeId.from(id));
    }
}
