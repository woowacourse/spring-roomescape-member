package roomescape.service;

import static roomescape.exception.ExceptionType.DELETE_USED_THEME;
import static roomescape.exception.ExceptionType.DUPLICATE_THEME;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        boolean hasDuplicateTheme = themeRepository.findAll().stream()
                .anyMatch(theme -> theme.isNameOf(themeRequest.name()));
        if (hasDuplicateTheme) {
            throw new RoomescapeException(DUPLICATE_THEME);
        }
        Theme saved = themeRepository.save(
                new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail()));
        return toResponse(saved);
    }

    private ThemeResponse toResponse(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ThemeResponse> findAndOrderByPopularity(LocalDate start, LocalDate end, int count) {
        return themeRepository.findAndOrderByPopularity(start, end, count).stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(long id) {
        //todo : 변수명 고민
        boolean invalidDelete = reservationRepository.findAll().stream()
                .anyMatch(reservation -> reservation.isThemeOf(id));
        if (invalidDelete) {
            throw new RoomescapeException(DELETE_USED_THEME);
        }
        themeRepository.delete(id);
    }
}
