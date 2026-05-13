package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int RANKS_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        if (themeRepository.existsByName(themeRequest.name())) {
            throw new RoomescapeException(ErrorCode.THEME_DUPLICATED);
        }
        Theme theme = themeRepository.save(themeRequest.name(), themeRequest.description(), themeRequest.url());
        return ThemeResponse.from(theme);
    }

    public void removeById(Long id) {
        themeRepository.findById(id).orElseThrow(
                () -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND)
        );
        if (reservationRepository.existsByThemeId(id)) {
            throw new RoomescapeException(ErrorCode.THEME_HAS_RESERVATIONS);
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> readAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }

    public List<ThemeResponse> readRanks(LocalDate today) {
        LocalDate endDate = today.minusDays(1);
        LocalDate startDate = today.minusDays(7);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(endDate,
                startDate,
                RANKS_LIMIT_COUNT);
        return themes.stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
    }
}
