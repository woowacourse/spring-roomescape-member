package roomescape.repository.condition;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import roomescape.dto.reservation.request.ReservationQueryRequest;

public class Conditions {

    private List<Condition> conditions = new ArrayList<>();
    private List<Object> args = new ArrayList<>();
    private List<Integer> argTypes = new ArrayList<>();

    public Conditions(ReservationQueryRequest reservationRequest) {
        checkThemeIdCondition(reservationRequest);
        checkMemberIdCondition(reservationRequest);
        checkDateFromCondition(reservationRequest);
        checkDateToCondition(reservationRequest);
    }

    public String createQuery(String baseSql) {
        if (conditions.isEmpty()) {
            return baseSql;
        }
        return createDynamicQuery(baseSql);
    }

    private String createDynamicQuery(String baseSql) {
        String conditionPhrases = conditions.stream()
                .map(Condition::getConditionPhrase)
                .collect(Collectors.joining(" AND "));

        return new StringBuilder(baseSql)
                .append("WHERE ")
                .append(conditionPhrases)
                .toString();
    }

    private void checkThemeIdCondition(ReservationQueryRequest reservationRequest) {
        if (reservationRequest.themeId() != null) {
            conditions.add(new EqualsTo("theme_id"));
            args.add(reservationRequest.themeId());
            argTypes.add(Types.BIGINT);
        }
    }

    private void checkMemberIdCondition(ReservationQueryRequest reservationRequest) {
        if (reservationRequest.memberId() != null) {
            conditions.add(new EqualsTo("member_id"));
            args.add(reservationRequest.memberId());
            argTypes.add(Types.BIGINT);
        }
    }

    private void checkDateFromCondition(ReservationQueryRequest reservationRequest) {
        if (reservationRequest.dateFrom() != null) {
            conditions.add(new BiggerInclusiveThan("date"));
            args.add(reservationRequest.dateFrom());
            argTypes.add(Types.DATE);
        }
    }

    private void checkDateToCondition(ReservationQueryRequest reservationRequest) {
        if (reservationRequest.dateTo() != null) {
            conditions.add(new SmallerInclusiveThan("date"));
            args.add(reservationRequest.dateTo());
            argTypes.add(Types.DATE);
        }
    }

    public Object[] getArgs() {
        return this.args.toArray();
    }

    public int[] getArgTypes() {
        return this.argTypes.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }
}
