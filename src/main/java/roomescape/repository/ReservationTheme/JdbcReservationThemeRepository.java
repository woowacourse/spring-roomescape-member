package roomescape.repository.ReservationTheme;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationThemeDao;
import roomescape.domain.ReservationTheme.PopularThemeCondition;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.domain.ReservationTheme.ReservationThemeDaoData;
import roomescape.domain.ReservationTheme.ReservationThemeWithCount;
import roomescape.domain.ReservationTheme.ReservationThemeWithCountDaoData;

@Repository
public class JdbcReservationThemeRepository implements ReservationThemeRepository {
    private final ReservationThemeDao reservationThemeDao;

    public JdbcReservationThemeRepository(ReservationThemeDao reservationThemeDao) {
        this.reservationThemeDao = reservationThemeDao;
    }

    public ReservationTheme addTheme(ReservationThemeCommand reservationThemeCommand) {
        ReservationThemeDaoData reservationThemeDaoData = reservationThemeDao.addTheme(reservationThemeCommand);
        return createTheme(reservationThemeDaoData);
    }

    public List<ReservationTheme> getAllTheme() {
        List<ReservationThemeDaoData> themeDaoAllData = reservationThemeDao.getAllTheme();
        return themeDaoAllData.stream()
                .map(this::createTheme)
                .toList();
    }

    public Optional<ReservationTheme> getTheme(long id) {
        Optional<ReservationThemeDaoData> themeDaoData = reservationThemeDao.getTheme(id);

        if(themeDaoData.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(createTheme(reservationThemeDao.getTheme(id).get()));
    }

    public void deleteTheme(long id) {
        reservationThemeDao.deleteTheme(id);
    }

    public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
        return reservationThemeDao.getPopularTheme(popularThemeCondition).stream()
                .map(this::createReservationThemeWithCount)
                .toList();
    }

    private ReservationTheme createTheme(ReservationThemeDaoData reservationThemeDaoData) {
        return new ReservationTheme(
                reservationThemeDaoData.id(),
                reservationThemeDaoData.name(),
                reservationThemeDaoData.description(),
                reservationThemeDaoData.imageUrl()
        );
    }

    private ReservationThemeWithCount createReservationThemeWithCount(
            ReservationThemeWithCountDaoData reservationThemeWithCount) {
        return new ReservationThemeWithCount(
                reservationThemeWithCount.id(),
                reservationThemeWithCount.name(),
                reservationThemeWithCount.description(),
                reservationThemeWithCount.imageUrl(),
                reservationThemeWithCount.count()
        );
    }
}
