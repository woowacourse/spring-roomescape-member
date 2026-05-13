package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.TimeRow;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(TimeJdbcDao.class)
@ActiveProfiles("test")
class TimeJdbcDaoTest {

    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    @Autowired
    private TimeDao timeDao;

    private TimeRow row(int hour) {
        return new TimeRow(LocalTime.of(hour, 0));
    }

    @Nested
    @DisplayName("create는")
    class Create {

        @Test
        void 저장_후_ID가_채워진_객체를_반환한다() {
            TimeRow time = row(10);

            TimeRow saved = timeDao.create(time);

            assertThat(saved.id()).isNotNull();
            assertThat(saved.startAt()).isEqualTo(time.startAt());
        }

        @Test
        void 같은_start_at이면_DuplicateKeyException() {
            TimeRow time = row(10);

            timeDao.create(time);

            assertThatThrownBy(() -> timeDao.create(time))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Nested
    @DisplayName("findById는")
    class FindById {

        @Test
        void 존재하면_Optional로_감싸_반환한다() {
            TimeRow time = row(10);
            TimeRow saved = timeDao.create(time);

            Optional<TimeRow> found = timeDao.findById(saved.id());
            assertThat(found).isPresent()
                    .get()
                    .isEqualTo(saved);
        }

        @Test
        void 존재하지_않으면_Optional_empty() {
            assertThat(timeDao.findById(NOT_EXISTS_ID)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll은")
    class FindAll {

        @Test
        void 저장된_모든_시간을_반환한다() {
            TimeRow time1 = row(10);
            TimeRow time2 = row(11);

            TimeRow first = timeDao.create(time1);
            TimeRow second = timeDao.create(time2);

            List<TimeRow> all = timeDao.findAll();

            assertThat(all).hasSize(2).contains(first, second);
        }

        @Test
        void 비어있으면_빈_리스트() {
            assertThat(timeDao.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete는")
    class Delete {

        @Test
        void 존재하는_ID는_1를_반환하고_삭제한다() {
            TimeRow time = row(10);
            TimeRow saved = timeDao.create(time);

            int affected = timeDao.delete(saved.id());

            assertThat(affected).isEqualTo(1);
            assertThat(timeDao.findById(saved.id())).isEmpty();
        }

        @Test
        void 존재하지_않는_ID는_0을_반환한다() {
            assertThat(timeDao.delete(NOT_EXISTS_ID)).isZero();
        }

        @Test
        void 삭제_후_같은_시간으로_재등록_가능() {
            TimeRow time = row(10);
            TimeRow saved = timeDao.create(time);
            timeDao.delete(saved.id());

            assertThatCode(() -> timeDao.create(time))
                    .doesNotThrowAnyException();
        }
    }
}
