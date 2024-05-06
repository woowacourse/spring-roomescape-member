package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return ThemeResponse.from(themeRepository.save(theme));
    }

    public void delete(long themeId) {
        if (reservationRepository.existsByThemeId(themeId)) {
            throw new IllegalArgumentException(String.format("예약이 존재하는 테마입니다. id=%d를 확인해주세요.", themeId));
        }

        if (!themeRepository.deleteById(themeId)) {
            throw new IllegalArgumentException(String.format("테마 삭제에 실패했습니다. id=%d를 확인해주세요.", themeId));
        }

    }

    public List<ThemeResponse> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        return themeRepository.findPopularThemes(startDate, endDate, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
