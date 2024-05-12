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
        if (id != null) {
            appendBigIntSimpleCondition(id, "theme_id");
        }

        return this;
    }

    public ReservationQueryConditionsBuilder timeId(Long id) {
        if (id != null) {
            appendBigIntSimpleCondition(id, "time_id");
        }

        return this;
    }

    public ReservationQueryConditionsBuilder memberId(Long id) {
        if (id != null) {
            appendBigIntSimpleCondition(id, "member_id");
        }

        return this;
    }

    private void appendBigIntSimpleCondition(Long id, String column) {
        SimpleCondition simpleCondition = new SimpleCondition(column);
        conditions.add(simpleCondition);
        args.add(id);
        argTypes.add(Types.BIGINT);
    }

    public ReservationQueryConditionsBuilder date(String date) {
        if (date == null) {
            return this;
        }

        SimpleCondition simpleCondition = new SimpleCondition("date");
        conditions.add(simpleCondition);
        args.add(date);
        argTypes.add(Types.VARCHAR);

        return this;
    }

    public ReservationQueryConditionsBuilder period(String from, String end) {
        if (from == null || end == null) {
            return this;
        }

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(end);
        validatePeriod(fromDate, endDate);
        appendPeriodCondition(fromDate, endDate);

        return this;
    }

    private void validatePeriod(LocalDate fromDate, LocalDate endDate) {
        if (fromDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 조건 날짜는 종료 조건 날짜보다 미래일 수 없습니다.");
        }
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
