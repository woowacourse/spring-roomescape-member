package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationThemeDao;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationThemeResponse;
import roomescape.entity.ReservationThemeEntity;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;

import java.util.List;

@Service
public class ReservationThemeService {
    private final ReservationThemeDao themeDao;

    public ReservationThemeService(ReservationThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ReservationThemeResponse createTheme(ReservationThemeRequest request) {
        ReservationThemeEntity newTheme = request.toEntity();
        // TODO: Optional 개선하기
        if (themeDao.findByName(newTheme.getName()).isPresent()) {
            throw new ConflictException("중복되는 테마가 존재합니다.");
        }
        ReservationThemeEntity saved = themeDao.save(newTheme);
        return ReservationThemeResponse.from(saved);
    }

    public List<ReservationThemeResponse> getAllThemes() {
        return themeDao.findAll()
                .stream()
                .map(ReservationThemeResponse::from)
                .toList();
    }

    public void deleteTheme(final Long id) {
        final boolean deleted = themeDao.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }
}
