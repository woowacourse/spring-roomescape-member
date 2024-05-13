package roomescape.reservation.provider;

import roomescape.reservation.dto.SearchInfo;

import java.util.ArrayList;
import java.util.List;

public class WhereQueryProvider {
    private final List<Object> params;
    private final List<String> conditions;

    public WhereQueryProvider() {
        this.params = new ArrayList<>();
        this.conditions = new ArrayList<>();
    }

    public String makeQuery(SearchInfo searchInfo) {
        exploreSearchInfo(searchInfo);
        return connectQueries();
    }

    public Object[] getParams() {
        return params.toArray();
    }

    private void exploreSearchInfo(SearchInfo searchInfo) {
        if (searchInfo.isMemberIdNotNull()) {
            conditions.add("m.id = ?");
            params.add(searchInfo.getMemberId());
        }
        if (searchInfo.isThemeIdNotNull()) {
            conditions.add("t.id = ?");
            params.add(searchInfo.getThemeId());
        }
        if (searchInfo.isDurationNotNull()) {
            conditions.add("r.date BETWEEN ? AND ?");
            params.add(searchInfo.getDateFrom());
            params.add(searchInfo.getDateTo());
        }
    }

    private String connectQueries() {
        String whereClause = String.join(" AND ", conditions);
        if (!whereClause.isEmpty()) {
            whereClause = " WHERE " + whereClause;
        }
        return whereClause;
    }
}
