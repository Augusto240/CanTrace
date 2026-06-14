package br.edu.cantrace.api;

import java.time.Instant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/db")
public class DatabasePingController {

    private static final String DATABASE_PING_SQL = """
        select current_database() as database_name,
               current_user as database_user,
               now() as server_time
        """;

    private final JdbcTemplate jdbcTemplate;

    public DatabasePingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/ping")
    public DatabasePingResponse ping() {
        return jdbcTemplate.queryForObject(DATABASE_PING_SQL, (rs, rowNum) -> new DatabasePingResponse(
            "ok",
            rs.getString("database_name"),
            rs.getString("database_user"),
            rs.getObject("server_time").toString(),
            Instant.now().toString()
        ));
    }
}
