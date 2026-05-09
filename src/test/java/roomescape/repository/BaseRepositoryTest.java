package roomescape.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.UUID;

public abstract class BaseRepositoryTest {
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void baseSetUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUrl("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
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
                "start_at VARCHAR(255) NOT NULL)"
        );
    }

    protected void createReservationTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "date VARCHAR(255) NOT NULL, " +
                "time_id BIGINT NOT NULL," +
                "theme_id BIGINT NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (time_id) REFERENCES reservation_time (id)," +
                "FOREIGN KEY (theme_id) REFERENCES theme (id))"
        );
    }

    protected void createThemeTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS theme (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255) NOT NULL, " +
                "image_url VARCHAR(255) NOT NULL)"
        );
    }

    protected void insertReservationTime(String time) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", time);
    }

    protected void insertTheme(String name, String description, String imageUrl) {
        jdbcTemplate.update("INSERT INTO theme (name, description, image_url) VALUES (?, ?, ?)", name, description, imageUrl);
    }


    protected void insertReservation(String name, String date, long timeId, long themeId) {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", name, date, timeId, themeId);
    }

    protected void deleteReservationTable() {
        jdbcTemplate.execute("DROP TABLE reservation");
    }

    protected void deleteThemeTable() {
        jdbcTemplate.execute("DROP TABLE theme");
    }

    protected void deleteReservationTimeTable() {
        jdbcTemplate.execute("DROP TABLE reservation_time");
    }
}
