package roomescape.closeddate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.domain.ClosedDate;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;

@SpringBootTest
@Transactional
class ClosedDateServiceTest {

    @Autowired
    private ClosedDateService closedDateService;

    private static final LocalDate DEFAULT_DATE = LocalDate.of(2099, 1, 1);

    @Test
    @DisplayName("등록된 휴무일과 조회된 휴무일의 모든 필드는 일치한다.")
    void readClosedDate() {
        // given
        ClosedDate saved = closedDateService.register(DEFAULT_DATE);

        // when
        List<ClosedDate> actual = closedDateService.findClosedDates();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(saved));
    }

    @Test
    @DisplayName("등록된 휴무일이 여러개이면 조회 시 등록된 개수만큼 반환한다.")
    void findClosedDates() {
        // given
        closedDateService.register(DEFAULT_DATE);
        closedDateService.register(DEFAULT_DATE.plusDays(1));

        // when
        List<ClosedDate> actual = closedDateService.findClosedDates();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("휴무일을 1개 등록하면 데이터 수가 1 증가한다.")
    void register() {
        // given & when
        closedDateService.register(DEFAULT_DATE);

        // then
        assertThat(closedDateService.findClosedDates()).hasSize(1);
    }

    @Test
    @DisplayName("등록한 휴무일과 다시 조회한 휴무일의 모든 필드가 일치한다.")
    void register_fields_match() {
        // when
        ClosedDate registered = closedDateService.register(DEFAULT_DATE);

        // then
        assertThat(closedDateService.findClosedDates())
                .usingRecursiveComparison()
                .isEqualTo(List.of(registered));
    }

    @Test
    @DisplayName("이미 등록된 휴무일을 다시 등록하면 예외가 발생한다.")
    void register_duplicate_date() {
        // given
        closedDateService.register(DEFAULT_DATE);

        // when & then
        assertThatThrownBy(() -> closedDateService.register(DEFAULT_DATE))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 등록된 휴무일입니다.");
    }

    @Test
    @DisplayName("등록된 휴무일 2개 중 한 개를 삭제하면 데이터 수는 1개가 된다.")
    void deregister() {
        // given
        ClosedDate saved1 = closedDateService.register(DEFAULT_DATE);
        closedDateService.register(DEFAULT_DATE.plusDays(1));

        // when
        closedDateService.deregister(saved1.id());

        // then
        assertThat(closedDateService.findClosedDates()).hasSize(1);
    }

    @Test
    @DisplayName("등록되지 않은 휴무일을 삭제하면 예외가 발생한다.")
    void deregister_not_exists() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> closedDateService.deregister(wrongId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 휴무일입니다.");
    }
}
