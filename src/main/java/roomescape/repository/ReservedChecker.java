package roomescape.repository;

import java.time.LocalDate;

public interface ReservedChecker {
    boolean contains(LocalDate reservationDate, Long timeId, Long themeId);
}
