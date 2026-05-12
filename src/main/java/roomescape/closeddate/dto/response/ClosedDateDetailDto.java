package roomescape.closeddate.dto.response;

import java.time.LocalDate;
import roomescape.closeddate.domain.ClosedDate;

public record ClosedDateDetailDto(Long id, LocalDate date) {
    public static ClosedDateDetailDto from(ClosedDate closedDate) {
        return new ClosedDateDetailDto(closedDate.id(), closedDate.date());
    }
}
