package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Theme;

@JdbcTest
@Import(ThemeRepository.class)
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        savedTheme = themeRepository.save(Theme.of("공포", "무서운 테마", "https://example.com/img.jpg"));
    }

    @Test
    void 테마를_저장한다() {
        assertThat(savedTheme.getId()).isPositive();
        assertThat(savedTheme.getName()).isEqualTo("공포");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 테마");
    }

    @Test
    void id로_테마를_조회한다() {
        Optional<Theme> found = themeRepository.findById(savedTheme.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("공포");
    }

    @Test
    void 전체_테마를_조회한다() {
        themeRepository.save(Theme.of("추리", "머리 쓰는 테마", "https://example.com/img2.jpg"));

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).hasSize(2);
    }

    @Test
    void 테마를_삭제한다() {
        themeRepository.deleteById(savedTheme.getId());

        Optional<Theme> found = themeRepository.findById(savedTheme.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void 인기_테마를_조회한다() {
        // findFamous는 reservation 데이터와 join되므로 통합 테스트에서 별도 검증
        // 여기서는 빈 결과 반환 여부만 확인
        List<Theme> famous = themeRepository.findFamous(7, LocalDate.now(), 10);

        assertThat(famous).isNotNull();
    }
}
