package roomescape.dao;

import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.response.AvailableTimeResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ThemeDao extends CommonDao<Theme> {
    boolean existsByName(Name name);
    List<AvailableTimeResponseDto> findAvailableTimesById(Long themeId, LocalDate localDate);
}
