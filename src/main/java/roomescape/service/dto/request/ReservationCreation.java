package roomescape.service.dto.request;

import java.time.LocalDate;
import roomescape.controller.dto.request.CreateReservationForAdminRequest;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.LoginMember;

public record ReservationCreation(long memberId, LocalDate date, long timeId, long themeId) {

    public static ReservationCreation of(final LoginMember member, final CreateReservationRequest request) {
        return new ReservationCreation(member.id(), request.date(), request.timeId(), request.themeId());
    }

    public static ReservationCreation from(final CreateReservationForAdminRequest request) {
        return new ReservationCreation(request.memberId(), request.date(), request.timeId(), request.themeId());
    }
}
