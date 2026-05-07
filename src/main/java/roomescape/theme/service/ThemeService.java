package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.dao.ThemeDAO;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeCreateRequest;
import roomescape.theme.dto.response.ReservedThemeResponse;

@Service
public class ThemeService {

    private final ThemeDAO themeDAO;

    public ThemeService(ThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }

    public List<Theme> findAll() {
        return themeDAO.findAll();
    }

    public Theme create(ThemeCreateRequest request) {
        Theme theme = new Theme(
                request.name(),
                request.description(),
                request.imageUrl()
        );

        return themeDAO.insert(theme);
    }

    public List<ReservedThemeResponse> findMostReserved(long limit, LocalDate startDate, LocalDate endDate) {
        LocalDate notNullEndDate = handleDefaultEndDate(endDate);
        return themeDAO.findMostReserved(limit, startDate, notNullEndDate);
    }

    private LocalDate handleDefaultEndDate(LocalDate endDate) {
        if (endDate == null) {
            return LocalDate.now().minusDays(1);
        }
        return endDate;
    }

    public void delete(long id) {
        boolean deleted = themeDAO.delete(id);
        if (!deleted) {
            throw new IllegalArgumentException("삭제할 테마를 조회하지 못했습니다. id = " + id);
        }
    }
}
