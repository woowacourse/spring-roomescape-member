package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.repository.ReservationThemeRepository;
import roomescape.theme.dto.ReservationThemeRequest;
import roomescape.theme.dto.ReservationThemeResponse;
import roomescape.theme.entity.ReservationThemeEntity;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationThemeService {
    private final ReservationThemeRepository themeRepository;

    public ReservationThemeService(ReservationThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ReservationThemeResponse createTheme(ReservationThemeRequest request) {
        ReservationThemeEntity newTheme = request.toEntity();
        // TODO: Optional 개선하기
        if (themeRepository.findByName(newTheme.getName()).isPresent()) {
            throw new ConflictException("중복되는 테마가 존재합니다.");
        }
        ReservationThemeEntity saved = themeRepository.save(newTheme);
        return ReservationThemeResponse.from(saved);
    }

    public List<ReservationThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }

    public void deleteTheme(final Long id) {
        final boolean deleted = themeRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    public List<ReservationThemeResponse> getPopularThemes(final int limit) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);
        return themeRepository.findPopularDescendingUpTo(startDate, endDate, limit)
                .stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }
}
