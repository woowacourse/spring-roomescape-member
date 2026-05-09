package roomescape.dao;


import roomescape.dao.row.TimeRow;

import java.time.LocalTime;

public interface TimeDao extends CommonDao<TimeRow> {
    boolean existsByStartAt(LocalTime startAt);
}
