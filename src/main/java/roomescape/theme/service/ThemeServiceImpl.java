package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository, ReservationTimeRepository reservationTimeRepository) {
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    @Override
    public Theme createTheme(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        return themeRepository.save(theme);
    }

    @Transactional
    @Override
    public void removeTheme(Long id) {
        themeRepository.remove(id);
    }

    @Override
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Override
    public List<AvailableTime> getAvailableTimes(Long id, LocalDate date){
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Long> availableTimes = themeRepository.findNotAvailableTimes(id, date);

        return reservationTimes.stream()
                .map(t -> new AvailableTime(t.getId(), t.getStartAt(), !availableTimes.contains(t.getId())))
                .toList();
    }

    @Override
    public List<PopularTheme> getPopularThemes(int days, int limit) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        List<Theme> themes = themeRepository.findPopularThemes(endDate, startDate, limit);

        return themes.stream()
                .map(theme -> new PopularTheme(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail(),
                        themes.indexOf(theme) + 1))
                .toList();
    }
}
