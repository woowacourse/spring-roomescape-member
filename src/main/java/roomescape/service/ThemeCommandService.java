package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeCommandService {

    private final ThemeDao themeDao;

    public Theme create(String name, String thumbnailUrl, String description) {
        return themeDao.save(Theme.pending(name, thumbnailUrl, description));
    }

    public void delete(long themeId) {
        try {
            themeDao.delete(themeId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
