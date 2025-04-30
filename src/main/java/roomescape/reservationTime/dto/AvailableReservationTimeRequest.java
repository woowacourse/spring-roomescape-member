package roomescape.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AvailableReservationTimeRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date, Long themeId) {
}
