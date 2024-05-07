package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record FindReservationResponse(Long id,
                                      String name,
                                      LocalDate date,
                                      ReservationTime time,
                                      Theme theme) { }
