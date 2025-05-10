package roomescape.reservation.service.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.CreateAdminReservationRequest;
import roomescape.reservation.controller.dto.CreateUserReservationRequest;

public record CreateReservationServiceRequest(LocalDate date, long timeId, long themeId, long memberId) {

    public static CreateReservationServiceRequest fromUserRequestAndMember(final CreateUserReservationRequest request,
                                                                           final Member member) {
        return new CreateReservationServiceRequest(
                request.date(), request.timeId(), request.themeId(), member.getId());
    }

    public static CreateReservationServiceRequest fromAdminRequestAndMember(
            final CreateAdminReservationRequest request) {
        return new CreateReservationServiceRequest(
                request.date(), request.timeId(), request.themeId(), request.memberId());
    }
}
