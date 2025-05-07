package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Customer;

@Repository
public class CustomerDao {

    private final RowMapper<Customer> userRowMapper = (resultSet, rowNum) -> {
        return new Customer(
                resultSet.getString("email"),
                resultSet.getString("name"),
                resultSet.getString("password"));
    };

    private final JdbcTemplate jdbcTemplate;

    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(Customer customer){
        String sql = "SELECT EXISTS(SELECT 1 FROM customer WHERE email = ? AND password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, customer.getEmail(), customer.getPassword()));
    }
}
