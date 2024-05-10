package roomescape.reservation.repository.param;

import roomescape.reservation.dto.SearchReservationsParams;

import java.util.ArrayList;
import java.util.List;

public class QueryGenerator {
    public static String generateQueryWithSearchReservationsParams(final SearchReservationsParams params, final String baseQuery) {
        boolean isFirst = true;
        List<SearchParam> searchParams = new ArrayList<>();

        if (params.memberId() != null) {
            searchParams.add(new SearchDataParam<>(isFirst, "member_id", params.memberId()));
            isFirst = false;
        }

        if (params.themeId() != null) {
            searchParams.add(new SearchDataParam<>(isFirst, "theme_id", params.themeId()));
            isFirst = false;
        }

        searchParams.add(new SearchDateParam(isFirst,"date", params.from(), params.to()));
        return generateQueryWithSearchParams(searchParams, baseQuery);
    }

    private static String generateQueryWithSearchParams(final List<SearchParam> searchParams, final String baseQuery) {
        String generateQuery = baseQuery;
        for (SearchParam searchParam : searchParams) {
            generateQuery = searchParam.addParamToQuery(generateQuery);
        }

        return generateQuery;
    }
}
