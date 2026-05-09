package roomescape.closeddate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.closeddate.domain.ClosedDate;

@JdbcTest
class ClosedDateRepositoryTest {
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2099, 1, 1);
    private JdbcClosedDateRepository closedDateRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        closedDateRepository = new JdbcClosedDateRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("등록된 휴무일과 조회된 휴무일의 모든 필드는 일치한다.")
    void findById() {
        // given
        ClosedDate saved = closedDateRepository.save(ClosedDate.create(DEFAULT_DATE));

        // when
        ClosedDate actual = closedDateRepository.findById(saved.id()).get();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("등록된 휴무일이 여러개이면 조회 시 등록된 개수만큼 반환한다.")
    void findAll() {
        // given
        saveAll(List.of(
                ClosedDate.create(DEFAULT_DATE),
                ClosedDate.create(DEFAULT_DATE.plusDays(1))));

        // when
        List<ClosedDate> actual = closedDateRepository.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("휴무일을 1개 등록하면 데이터 수가 1 증가한다.")
    void save() {
        // given & when
        closedDateRepository.save(ClosedDate.create(DEFAULT_DATE));

        // then
        assertThat(closedDateRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("등록된 휴무일 2개 중 한 개를 삭제하면 데이터 수는 1개가 된다.")
    void delete() {
        // given
        List<ClosedDate> saved = saveAll(List.of(
                ClosedDate.create(DEFAULT_DATE),
                ClosedDate.create(DEFAULT_DATE.plusDays(1))));

        // when
        closedDateRepository.delete(saved.get(0).id());

        // then
        assertThat(closedDateRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("등록된 날짜로 존재 여부를 확인한다.")
    void existsByDate() {
        // given
        closedDateRepository.save(ClosedDate.create(DEFAULT_DATE));

        // when & then
        assertThat(closedDateRepository.existsByDate(DEFAULT_DATE)).isTrue();
        assertThat(closedDateRepository.existsByDate(DEFAULT_DATE.plusDays(1))).isFalse();
    }

    private List<ClosedDate> saveAll(List<ClosedDate> closedDates) {
        List<ClosedDate> saved = new ArrayList<>();
        for (ClosedDate closedDate : closedDates) {
            saved.add(closedDateRepository.save(closedDate));
        }
        return saved;
    }
}
