package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;

public record CreateUserReservationRequest(LocalDate date, long timeId, long themeId) {

    public CreateReservationServiceRequest toCreateReservationServiceRequest(final Member member) {
        return new CreateReservationServiceRequest(date, timeId, themeId, member.getId());
    }
}
