package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public record FindReservationResponse(Long id,
                                      String memberName,
                                      LocalDate date,
                                      @JsonFormat(pattern = "HH:mm") LocalTime time,
                                      String themeName) { }
