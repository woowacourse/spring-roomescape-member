package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.auth.dto.MemberInfo;
import roomescape.reservation.service.converter.ReservationConverter;
import roomescape.reservation.service.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.controller.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.controller.dto.CreateReservationWebRequest;
import roomescape.reservation.controller.dto.ReservationWebResponse;
import roomescape.reservation.service.usecase.ReservationCommandUseCase;
import roomescape.reservation.service.usecase.ReservationQueryUseCase;
import roomescape.theme.domain.ThemeId;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationQueryUseCase reservationQueryUseCase;
    private final ReservationCommandUseCase reservationCommandUseCase;

    public List<ReservationWebResponse> getAll() {
        return ReservationConverter.toDto(
                reservationQueryUseCase.getAll());
    }

    public List<AvailableReservationTimeWebResponse> getAvailable(final LocalDate date, final Long id) {
        final AvailableReservationTimeServiceRequest serviceRequest = new AvailableReservationTimeServiceRequest(
                date,
                ThemeId.from(id));

        return reservationQueryUseCase.getTimesWithAvailability(serviceRequest).stream()
                .map(ReservationConverter::toWebDto)
                .toList();
    }

    public ReservationWebResponse create(final CreateReservationWebRequest createReservationWebRequest, final MemberInfo memberInfo) {
        return ReservationConverter.toDto(
                reservationCommandUseCase.create(
                        new CreateReservationServiceRequest(
                                memberInfo.id(),
                                createReservationWebRequest.date(),
                                createReservationWebRequest.timeId(),
                                createReservationWebRequest.themeId())));
    }

    public void delete(final Long id) {
        reservationCommandUseCase.delete(ReservationId.from(id));
    }
}
