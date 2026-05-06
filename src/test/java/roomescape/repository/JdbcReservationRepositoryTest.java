package roomescape.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdbcReservationRepositoryTest {

    @Autowired
    JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @DisplayName("예약시간을 저장한다")
    @Test
    void 예약시간을_저장하면_id를_부여한다() {
        // given

        // when

        // then
    }

    @DisplayName("예약시간을 id로 조회한다")
    @Test
    void 예약시간을_id로_조회한다() {
        // given

        // when

        // then
    }

    @DisplayName("저장된 모든 예약시간을 조회한다")
    @Test
    void 저장된_모든_예약시간을_조회한다() {
        // given

        // when

        // then
    }

    @DisplayName("id에 해당하는 예약시간을 삭제한다")
    @Test
    void 예약시간을_삭제한다() {
        // given

        // when

        // then
    }
}
