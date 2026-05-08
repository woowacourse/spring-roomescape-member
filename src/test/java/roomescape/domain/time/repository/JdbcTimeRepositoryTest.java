package roomescape.domain.time.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.time.entity.Time;

class JdbcTimeRepositoryTest {

    private JdbcTimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
            "jdbc:h2:mem:" + System.nanoTime() + ";MODE=MySQL;DB_CLOSE_DELAY=-1",
            "sa",
            ""
        );

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        timeRepository = new JdbcTimeRepository(dataSource);
    }

    @Nested
    class SaveTest {

        @Test
        void 성공() {
            // given
            Time time = Time.create(LocalTime.of(10, 30));

            // when
            Time actual = timeRepository.save(time);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getStartAt()).isEqualTo(LocalTime.of(10, 30));
        }
    }

    @Nested
    class FindAllTimesTest {

        @Test
        void 성공() {
            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 30)));
            Time time3 = timeRepository.save(Time.create(LocalTime.of(13, 0)));

            // when
            List<Time> actual = timeRepository.findAllTimes();

            // then
            assertThat(actual)
                .extracting(Time::getId, Time::getStartAt)
                .containsExactly(
                    tuple(time1.getId(), time1.getStartAt()),
                    tuple(time2.getId(), time2.getStartAt()),
                    tuple(time3.getId(), time3.getStartAt())
                );
        }
    }

    @Nested
    class FindTimeByIdTest {

        @Test
        void 성공() {
            // given
            Time savedTime = timeRepository.save(Time.create(LocalTime.of(15, 30)));

            // when
            Optional<Time> actual = timeRepository.findTimeById(savedTime.getId());

            // then
            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isEqualTo(savedTime.getId());
            assertThat(actual.get().getStartAt()).isEqualTo(LocalTime.of(15, 30));
        }

        @Test
        void 존재하지_않으면_빈_값을_반환한다() {
            // when
            Optional<Time> actual = timeRepository.findTimeById(1L);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class DeleteTimeByIdTest {

        @Test
        void 성공() {
            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 0)));

            // when
            timeRepository.deleteTimeById(time1.getId());

            // then
            assertThat(timeRepository.findAllTimes())
                .extracting(Time::getId, Time::getStartAt)
                .containsExactly(tuple(time2.getId(), time2.getStartAt()));
        }
    }
}
