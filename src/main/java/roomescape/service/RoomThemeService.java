package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.PopularThemeResponse;
import roomescape.controller.dto.response.RoomThemeResponse;
import roomescape.dao.ReservationDAO;
import roomescape.dao.RoomThemeDAO;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.exception.custom.PharmaceuticalViolationException;
import roomescape.service.dto.RoomThemeCreation;

@Service
public class RoomThemeService {

    private final ReservationDAO reservationDAO;
    private final RoomThemeDAO themeDAO;

    public RoomThemeService(final ReservationDAO reservationDAO, final RoomThemeDAO themeDAO) {
        this.reservationDAO = reservationDAO;
        this.themeDAO = themeDAO;
    }

    public RoomThemeResponse addTheme(final RoomThemeCreation themeCreation) {
        if (themeDAO.existsByName(themeCreation.name())) {
            throw new ExistedDuplicateValueException("이미 존재하는 테마입니다");
        }

        final RoomTheme theme = new RoomTheme(themeCreation.name(), themeCreation.description(),
                themeCreation.thumbnail());
        final long id = themeDAO.insert(theme);

        final RoomTheme savedTheme = findById(id);
        return RoomThemeResponse.from(savedTheme);
    }

    private RoomTheme findById(final long id) {
        return themeDAO.findById(id)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 테마입니다"));
    }

    public List<RoomThemeResponse> findAllThemes() {
        return themeDAO.findAll()
                .stream()
                .map(RoomThemeResponse::from)
                .toList();
    }

    public List<PopularThemeResponse> findPopularThemes() {
        final LocalDate currentDate = LocalDate.now();
        final LocalDate start = currentDate.minusDays(8);
        final LocalDate end = currentDate.minusDays(1);

        return themeDAO.findPopularThemes(start, end).stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    public void deleteTheme(final long id) {
        if (reservationDAO.existsByThemeId(id)) {
            throw new PharmaceuticalViolationException("사용 중인 테마입니다");
        }

        final boolean deleted = themeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 테마입니다");
        }
    }
}
