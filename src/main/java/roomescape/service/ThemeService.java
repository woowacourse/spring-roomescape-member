package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.model.Theme;

@Service
public class ThemeService {

    private static final int POPULAR_DAY_RANGE = 7;

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
    }

    public ThemeResponseDto saveTheme(final ThemeRequestDto themeRequestDto) {
        validateTheme(themeRequestDto);

        Theme theme = themeRequestDto.convertToTheme();
        Long savedId = themeDao.saveTheme(theme);

        return new ThemeResponseDto(
                savedId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    private void validateTheme(final ThemeRequestDto themeRequestDto) {
        boolean duplicatedNameExisted = themeDao.isDuplicatedNameExisted(themeRequestDto.name());
        if (duplicatedNameExisted) {
            throw new DuplicatedException("중복된 예약시각은 등록할 수 없습니다.");
        }
    }

    public void deleteTheme(final Long id) {
        checkExistence(id);

        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceInUseException("삭제하고자 하는 테마에 예약된 정보가 있습니다.");
        }
    }

    private void checkExistence(final Long id) {
        Optional<Theme> foundTheme = themeDao.findById(id);
        if (foundTheme.isPresent()) {
            throw new NotFoundException("해당 id 와 일치하는 테마가 존재하지 않습니다.");
        }
    }

    public List<ThemeResponseDto> findPopularThemes(final String date) {
        LocalDate parsedDate = LocalDate.parse(date);

        return themeDao.findPopularThemes(parsedDate, POPULAR_DAY_RANGE).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
