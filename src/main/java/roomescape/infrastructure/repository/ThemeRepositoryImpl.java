package roomescape.infrastructure.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.domain.exception.ReservationExistException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.exception.ThemeDuplicatedException;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.infrastructure.dao.ThemeDao;

import java.util.List;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepositoryImpl(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Override
    public Theme save(final Theme theme) {
        try {
            return themeDao.save(theme);
        } catch (DuplicateKeyException e) {
            throw new ThemeDuplicatedException();
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
            throw new ReservationExistException();
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
