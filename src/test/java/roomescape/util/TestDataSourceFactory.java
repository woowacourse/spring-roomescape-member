package roomescape.util;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class TestDataSourceFactory {

    public static DataSource getEmbeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("reset-table.sql")
                .addScript("data.sql")
                .build();
    }

}
