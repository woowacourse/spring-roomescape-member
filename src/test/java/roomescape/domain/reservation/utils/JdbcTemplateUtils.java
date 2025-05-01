package roomescape.domain.reservation.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class JdbcTemplateUtils {

    private JdbcTemplateUtils() {
    }

    public static void deleteAllTables(JdbcTemplate jdbcTemplate) {
        try {
            // 외래 키 제약 테이블 삭제
            jdbcTemplate.update("truncate TABLE reservation");

            // 부모 테이블 삭제
            jdbcTemplate.update("delete from reservation_time");
            jdbcTemplate.update("delete from theme");
        } catch (DataAccessException e) {
            throw new RuntimeException("테이블 삭제 중 오류 발생", e);
        }
    }
}
