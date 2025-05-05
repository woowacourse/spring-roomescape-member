package roomescape.reservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;

public record ReservationDetailResponse(

        long id,

        MemberResponse member,

        ThemeResponse theme,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        TimeResponse time
) {
    public record MemberResponse(long id, String name) {
    }

    public record ThemeResponse(long id, String name, String description, String thumbnail) {
    }

    public record TimeResponse(
            long id,
            @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    }

    public static ReservationDetailResponse from(ReservationDetailData detailData) {
        return new ReservationDetailResponse(
                detailData.id(),
                new MemberResponse(detailData.member().id(), detailData.member().name()),
                new ThemeResponse(detailData.theme().id(), detailData.theme().name(),
                        detailData.theme().description(), detailData.theme().thumbnail()),
                detailData.date(),
                new TimeResponse(detailData.time().id(), detailData.time().startAt())
        );
    }

    public static List<ReservationDetailResponse> from(List<ReservationDetailData> detailDatas) {
        return detailDatas.stream()
                .map(ReservationDetailResponse::from)
                .toList();
    }
}
