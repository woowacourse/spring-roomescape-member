package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.member.LoginMember;

public record CreateReservationServiceRequest(LocalDate date, long timeId, long themeId, long memberId) {

    public static CreateReservationServiceRequest fromRequestAndMember(final CreateReservationRequest request,
                                                                       final LoginMember member) {
        return new CreateReservationServiceRequest(
                request.date(), request.timeId(), request.themeId(), member.getId());
    }
}
