package be.vdab.cinefest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql({ "/films.sql", "/reservaties.sql" })
@AutoConfigureMockMvc
public class ReservatieControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String RESERVATIES = "reservaties";
    private final MockMvc mockMvc;

    public ReservatieControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    @Test
    void findByEmailAdres() throws Exception {
        //mockMvc.perform(get("/reservaties").param("emailAdres", "test@example.org"))
        mockMvc.perform(get("/reservaties?emailAdres={emailAdres}", "test@example.org"))
                .andExpectAll(status().isOk(),
                        jsonPath("length()").value(
                                countRowsInTableWhere(RESERVATIES, "emailAdres = 'test@example.org'")));
    }
}
