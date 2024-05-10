package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.domain.member.LoginMember;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;

public record CreateReservationResponse(Long id,
                                        LoginMember member,
                                        LocalDate date,
                                        ReservationTime time,
                                        Theme theme) { }
