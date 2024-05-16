package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.ReservationTerm;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@Service
public class ThemeService {
    private static final long MAXIMUM_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme newTheme = themeRepository.save(theme);
        return new ThemeResponse(newTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        validateByReservation(id);
        themeRepository.deleteById(id);
    }

    private void validateByReservation(long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new InvalidReservationException("해당 테마로 예약이 존재해서 삭제할 수 없습니다.");
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        ReservationTerm reservationTerm = ReservationTerm.of(7);
        List<Theme> themes = themeRepository.findByReservationTermAndCount(
                reservationTerm.getStartDate(), reservationTerm.getEndDate(), MAXIMUM_COUNT);
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
