package roomescape.domain.reservation.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.utils.JdbcTemplateUtils;

@Transactional
@JdbcTest
@Import(ReservationTimeDAO.class)
class ReservationTimeDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간 저장")
    @Nested
    class findById {
        @DisplayName("id에 따라 예약 시간을 반환한다.")
        @Test
        void test1() {
            // given
            final Long id = 1L;
            final LocalTime now = LocalTime.now();
            JdbcTemplateUtils.insertReservationTime(jdbcTemplate, id, now);

            // when
            final ReservationTime result = reservationTimeRepository.findById(id)
                    .get();

            // then
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getStartAt()).isEqualTo(now);
        }

        @DisplayName("예약 시간을 삭제한다.")
        @Nested
        class delete {

            @DisplayName("해당 ID를 삭제한다.")
            @Test
            void test1() {
                // given
                final Long id = 1L;
                final LocalTime now = LocalTime.of(9, 0);
                JdbcTemplateUtils.insertReservationTime(jdbcTemplate, id, now);

                // when
                assertThatCode(() -> reservationTimeRepository.deleteById(id)).doesNotThrowAnyException();
            }

            @DisplayName("해당 id가 없다면 예외를 반환한다.")
            @Test
            void test2() {
                assertThatThrownBy(() -> reservationTimeRepository.deleteById(1L)).isInstanceOf(
                        EntityNotFoundException.class);
            }

        }

        @DisplayName("예약 시간을 저장한다.")
        @Nested
        class save {

            @DisplayName("성공 테스트")
            @Test
            void test1() {
                // given
                final LocalTime now = LocalTime.now();
                final ReservationTime time = ReservationTime.withoutId(now);

                // when
                final ReservationTime saved = reservationTimeRepository.save(time);

                // then
                assertThat(saved.getId()).isNotNull();
                assertThat(saved.getStartAt()).isEqualTo(now);
            }

            @DisplayName("ID가 있다면 해당 ID로 데이터를 업데이트한다.")
            @Test
            void test5() {
                // given
                final Long id = 1L;
                final LocalTime now = LocalTime.of(9, 0);
                JdbcTemplateUtils.insertReservationTime(jdbcTemplate, id, now);

                // when
                final LocalTime changeTime = LocalTime.of(10, 0);
                final ReservationTime reservationTime = new ReservationTime(id, changeTime);
                final ReservationTime result = reservationTimeRepository.save(reservationTime);

                // then

                final SoftAssertions softly = new SoftAssertions();

                softly.assertThat(result.getId())
                        .isEqualTo(id);
                softly.assertThat(result.getStartAt())
                        .isEqualTo(changeTime);

                softly.assertAll();
            }

            @DisplayName("DB에 해당 ID가 없고, 객체에 ID가 존재하는데 저장 시 예외를 반환한다.")
            @Test
            void test6() {
                // given
                final Long id = 1L;
                final LocalTime now = LocalTime.of(9, 0);
                final ReservationTime reservationTime = new ReservationTime(id, now);

                // when & then
                assertThatThrownBy(() -> reservationTimeRepository.save(reservationTime)).isInstanceOf(
                        EntityNotFoundException.class);
            }
        }
    }
}
