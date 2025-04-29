package roomescape.reservationTime.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static roomescape.reservationTime.ReservationTimeTestDataConfig.DEFAULT_DUMMY_TIME;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.globalException.CustomException;
import roomescape.reservationTime.ReservationTimeTestDataConfig;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.fixture.ReservationTimeFixture;
import roomescape.reservationTime.repository.ReservationTimeRepositoryImpl;

@JdbcTest
@Import({ReservationTimeRepositoryImpl.class, ReservationTimeService.class, ReservationTimeTestDataConfig.class})
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService service;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationTimeTestDataConfig testDataConfig;

    @DisplayName("ReservationTime 객체를 ReservationTimeResDto로 변환할 수 있다")
    @Test
    void convertToReservationTimeResDto() {
        // given
        ReservationTime reservationTime = ReservationTimeFixture.create(DEFAULT_DUMMY_TIME);

        // when
        ReservationTimeResDto resDto = service.convertToReservationTimeResDto(reservationTime);

        // then
        Assertions.assertThat(resDto.startAt()).isEqualTo(DEFAULT_DUMMY_TIME);
    }

    private void deleteAll() {
        jdbcTemplate.update("delete from reservation_time");
    }

    @Nested
    @DisplayName("저장된 모든 예약 시간 불러오는 기능")
    class readAll {

        @DisplayName("데이터가 있을 때 모든 예약 시간을 불러온다")
        @Test
        void readAll_success_whenDataExists() {
            // given
            // when
            List<ReservationTimeResDto> resDtos = service.readAll();

            // then
            assertSoftly(s -> {
                    s.assertThat(resDtos).hasSize(1);
                    s.assertThat(resDtos)
                        .extracting(ReservationTimeResDto::startAt)
                        .contains(DEFAULT_DUMMY_TIME);
                    resDtos.forEach(resDto ->
                        s.assertThat(resDto.id()).isNotNull());
                }
            );
        }

        @DisplayName("데이터가 없더라도 예외 없이 빈 리스트를 반환한다")
        @Test
        void readAll_success_whenNoData() {
            // given
            deleteAll();

            // when
            List<ReservationTimeResDto> resDtos = service.readAll();

            // then
            Assertions.assertThat(resDtos).hasSize(0);
        }
    }

    @Nested
    @DisplayName("예약 시간 추가 기능")
    class add {

        @DisplayName("유효한 입력일 시 예약 시간이 추가된다")
        @Test
        void add_success_whenValidInput() {
            // given
            LocalTime dummyTime1 = LocalTime.of(12, 33);

            // when
            service.add(ReservationTimeFixture.createReqDto(dummyTime1));

            // then
            List<ReservationTimeResDto> resDtos = service.readAll();
            Assertions.assertThat(resDtos)
                .extracting(ReservationTimeResDto::startAt)
                .contains(dummyTime1);
        }
    }

    @Nested
    @DisplayName("예약 시간 삭제 기능")
    class delete {

        @DisplayName("존재하는 id로 요청 시 예약 시간이 삭제된다.")
        @Test
        void delete_success_withValidId() {
            // given
            service.delete(testDataConfig.getDefaultDummyTimeId());

            // when
            List<ReservationTimeResDto> resDtos = service.readAll();

            // then
            Assertions.assertThat(resDtos).hasSize(0);
        }

        @DisplayName("존재하지 않는 id로 요청 시 예외가 발생한다")
        @Test
        void delete_throwException_whenIdNotFound() {
            // given
            // when
            // then
            Assertions.assertThatCode(
                () -> service.delete(Long.MAX_VALUE)
            ).isInstanceOf(CustomException.class);
        }
    }
}
