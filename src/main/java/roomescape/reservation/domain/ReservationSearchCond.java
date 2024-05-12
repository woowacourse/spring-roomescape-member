package roomescape.reservation.domain;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NotNull
public class ReservationSearchCond {

    private static final int LIMIT_DAY = 30;

    private Long themeId;
    private Long memberId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public ReservationSearchCond() {
    }

    public ReservationSearchCond(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        validateDateInterval(dateFrom, dateTo);
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }


    public void validateDateInterval(LocalDate dateFrom, LocalDate dateTo) {
        if (ChronoUnit.DAYS.between(dateFrom, dateTo) > LIMIT_DAY) {
            throw new IllegalArgumentException(String.format("%d일 까지만 입력 가능합니다."));
        }
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}

