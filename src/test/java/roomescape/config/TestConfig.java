package roomescape.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestConfig {

    public static DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test_database;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");

        return dataSource;
    }

    public static JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        jdbcTemplate.setDataSource(getDataSource());
        jdbcTemplate.execute(getSchema());

        return jdbcTemplate;
    }

    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        namedParameterJdbcTemplate.update(getSchema(), Collections.emptyMap());

        return namedParameterJdbcTemplate;
    }

    public static String getSchema() {
        try {
            ClassPathResource resource = new ClassPathResource("schema.sql");

            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("파일을 가져오는 데 문제가 생겼습니다!");
        }
    }
}
