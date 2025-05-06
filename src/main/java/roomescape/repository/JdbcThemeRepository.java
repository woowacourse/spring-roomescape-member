package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.ResourceNotExistException;

import java.util.List;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final ThemeDao themeDao;

    public JdbcThemeRepository(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Override
    public Theme save(final Theme theme) {
        try {
            return themeDao.save(theme);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 테마 생성에 실패했습니다.");
        }
    }

    @Override
    public Theme findById(final Long themeId) {
        try {
            return themeDao.findById(themeId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        try {
            return themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("[ERROR] 해당 테마에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }
    }

    @Override
    public List<Theme> findPopular(final int count) {
        return themeDao.findPopular(count);
    }


    @Override
    public boolean existByName(final String name) {
        return themeDao.existByName(name);
    }
}
