package roomescape.infrastructure.persistence.dynamic;

import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationQueryConditionsBuilder {

    private final List<Condition> conditions = new ArrayList<>();
    private final List<Object> args = new ArrayList<>();
    private final List<Integer> argTypes = new ArrayList<>();

    public ReservationQueryConditionsBuilder themeId(Long id) {
        if (id == null) {
            return this;
        }

        SimpleCondition simpleCondition = new SimpleCondition("theme_id");
        conditions.add(simpleCondition);
        args.add(id);
        argTypes.add(Types.BIGINT);

        return this;
    }

    public ReservationQueryConditionsBuilder memberId(Long id) {
        if (id == null) {
            return this;
        }

        SimpleCondition simpleCondition = new SimpleCondition("member_id");
        conditions.add(simpleCondition);
        args.add(id);
        argTypes.add(Types.BIGINT);

        return this;
    }

    public ReservationQueryConditionsBuilder period(String from, String end) {
        if (from == null && end == null) {
            return this;
        }

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(end);
        if (fromDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 조건 날짜는 종료 조건 날짜보다 미래일 수 없습니다.");
        }

        appendPeriodCondition(fromDate, endDate);
        return this;
    }

    private void appendPeriodCondition(LocalDate fromDate, LocalDate endDate) {
        PeriodCondition periodCondition = new PeriodCondition("convert(date, DATE)");
        conditions.add(periodCondition);
        args.add(fromDate);
        args.add(endDate);
        argTypes.add(Types.DATE);
        argTypes.add(Types.DATE);
    }

    public ReservationQueryConditions build() {
        Object[] args = this.args.toArray();
        int[] argTypes = this.argTypes.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return new ReservationQueryConditions(conditions, args, argTypes);
    }
}
