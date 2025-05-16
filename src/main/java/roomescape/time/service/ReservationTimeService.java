package roomescape.time.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.time.service.converter.ReservationTimeConverter;
import roomescape.time.service.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.controller.dto.CreateReservationTimeWebRequest;
import roomescape.time.controller.dto.ReservationTimeWebResponse;
import roomescape.time.service.usecase.ReservationTimeCommandUseCase;
import roomescape.time.service.usecase.ReservationTimeQueryUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeQueryUseCase reservationTimeQueryUseCase;
    private final ReservationTimeCommandUseCase reservationTimeCommandUseCase;

    public List<ReservationTimeWebResponse> getAll() {
        return ReservationTimeConverter.toDto(
                reservationTimeQueryUseCase.getAll());
    }

    public ReservationTimeWebResponse create(final CreateReservationTimeWebRequest createReservationTimeWebRequest) {
        return ReservationTimeConverter.toDto(
                reservationTimeCommandUseCase.create(
                        new CreateReservationTimeServiceRequest(
                                createReservationTimeWebRequest.startAt())));
    }

    public void delete(final Long id) {
        reservationTimeCommandUseCase.delete(ReservationTimeId.from(id));
    }
}
