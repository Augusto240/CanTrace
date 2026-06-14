package br.edu.cantrace.api;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.ArgumentMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DatabasePingController.class)
@AutoConfigureMockMvc(addFilters = false)
class DatabasePingControllerTest {

    private static final String DATABASE_PING_SQL = """
        select current_database() as database_name,
               current_user as database_user,
               now() as server_time
        """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnDatabasePing() throws Exception {
        when(jdbcTemplate.queryForObject(eq(DATABASE_PING_SQL), ArgumentMatchers.<RowMapper<DatabasePingResponse>>any()))
            .thenReturn(new DatabasePingResponse("ok", "cantrace", "cantrace", "2026-06-14T10:00:00Z", "2026-06-14T10:00:01Z"));

        mockMvc.perform(get("/api/db/ping"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("cantrace"))
            .andExpect(jsonPath("$.databaseUser").value("cantrace"));
    }
}
