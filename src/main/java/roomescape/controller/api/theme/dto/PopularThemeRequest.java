package roomescape.controller.api.theme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record PopularThemeRequest(

        @NotNull(message = "조회 시작 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @NotNull(message = "조회 마지막 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @NotNull(message = "조회 건수는 필수입니다.")
        @Positive(message = "조회 건수는 최소 1건 이상이어야 합니다.")
        Integer limit
) {
}
