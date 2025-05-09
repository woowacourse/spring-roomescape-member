package roomescape.reservation.service.dto;

import java.time.LocalDate;
import roomescape.member.domain.LoginMember;
import roomescape.reservation.controller.dto.CreateReservationRequest;

public record CreateReservationServiceRequest(LocalDate date, long timeId, long themeId, long memberId) {

    public static CreateReservationServiceRequest fromRequestAndMember(final CreateReservationRequest request,
                                                                       final LoginMember member) {
        return new CreateReservationServiceRequest(
                request.date(), request.timeId(), request.themeId(), member.getId());
    }
}
