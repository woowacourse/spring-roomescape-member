package roomescape.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public abstract class BaseDaoTest {
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void baseSetUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        initTable();
    }

    @AfterEach
    void baseTearDown() {
        deleteTable();
    }

    protected abstract void initTable();
    protected abstract void deleteTable();

    protected void createReservationTimeTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation_time (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "start_at VARCHAR(255) NOT NULL)");
    }

    protected void createReservationTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "date VARCHAR(255) NOT NULL, " +
                "time_id BIGINT, " +
                "FOREIGN KEY (time_id) REFERENCES reservation_time (id))");
    }

    protected void insertReservationTime(String time) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", time);
    }

    protected void insertReservation(String name, String date, long timeId) {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", name, date, timeId);
    }

    protected void deleteReservationTable() {
        jdbcTemplate.execute("DROP TABLE reservation");
    }

    protected void deleteReservationTimeTable() {
        jdbcTemplate.execute("DROP TABLE reservation_time");
    }
}
