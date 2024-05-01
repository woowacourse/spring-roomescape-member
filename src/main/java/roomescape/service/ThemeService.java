package roomescape.service;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ExceptionType;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

//todo 테스트코드 작성
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
            throw new RoomescapeException(ExceptionType.DUPLICATE_THEME);
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

    public void delete(long id) {
        //todo : 변수명 고민
        boolean invalidDelete = reservationRepository.findAll().stream()
                .anyMatch(reservation -> reservation.isThemeOf(id));
        if (invalidDelete) {
            throw new RoomescapeException(ExceptionType.INVALID_DELETE_THEME);
        }
        themeRepository.delete(id);
    }
}
