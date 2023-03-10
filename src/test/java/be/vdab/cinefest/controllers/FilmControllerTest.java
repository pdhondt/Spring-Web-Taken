package be.vdab.cinefest.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql("/films.sql")
@AutoConfigureMockMvc
class FilmControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final Path TEST_RESOURCES = Path.of("src/test/resources");
    private final static String FILMS = "films";
    private final static String RESERVATIES = "reservaties";
    private final MockMvc mockMvc;

    FilmControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private long findIdTestFilm1() {
        return jdbcTemplate.queryForObject(
                "select id from films where titel = 'testfilm1'", Long.class);
    }

    @Test
    void findById() throws Exception {
        var id = findIdTestFilm1();
        mockMvc.perform(get("/films/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(id),
                        jsonPath("titel").value("testfilm1"));
    }

    @Test
    void findByIdGeeftNotFoundBijOnbestaandeFilm() throws Exception {
        mockMvc.perform(get("/films/{id}", Long.MAX_VALUE))
                .andExpect(
                        status().isNotFound());
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(countRowsInTable(FILMS)));
    }

    @Test
    void findByJaar() throws Exception {
        mockMvc.perform(get("/films").param("jaar", "1986"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(
                                countRowsInTableWhere(FILMS, "jaar = 1986")));
    }

    @Test
    void deleteVerwijdertFilm() throws Exception {
        var id = findIdTestFilm1();
        mockMvc.perform(delete("/films/{id}", id))
                .andExpect(status().isOk());
        assertThat(countRowsInTableWhere(FILMS, "id = " + id)).isZero();
    }

    @Test
    void create() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteFilm.json"));
        var responseBody = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(countRowsInTableWhere(FILMS,
                "titel = 'testfilm3' and id = " + responseBody)).isOne();
    }

    @ParameterizedTest
    @ValueSource(strings = {"filmMetLegeTitel.json", "filmMetNegatiefJaar.json",
            "filmZonderJaar.json", "filmZonderTitel.json"})
    void createMetVerkeerdeDataMislukt(String fileName) throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve(fileName));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchWijzigtTitel() throws Exception {
        var id = findIdTestFilm1();
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteTitelWijziging.json"));
        mockMvc.perform(patch("/films/{id}/titel", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isOk());
        assertThat(countRowsInTableWhere(FILMS,
                "titel = 'gewijzigdeTitel' and id = " + id)).isOne();
    }

    @Test
    void patchVanOnbestaandeFilmMislukt() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteTitelWijziging.json"));
        mockMvc.perform(patch("/films/{id}/titel", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"titelWijzigingMetLegeTitel.json", "titelWijzigingZonderTitel.json"})
    void patchMetFouteDataMislukt(String fileName) throws Exception {
        var id = findIdTestFilm1();
        var jsonData = Files.readString(TEST_RESOURCES.resolve(fileName));
        mockMvc.perform(patch("/films/{id}/titel", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void correcteReservatie() throws Exception {
        var id = findIdTestFilm1();
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteReservatie.json"));
        var responseBody = mockMvc.perform(post("/films/{id}/reservatie", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(countRowsInTableWhere(RESERVATIES, "emailAdres = 'test@vdab.be' and plaatsen = 1" +
                " and id = " + responseBody)).isOne();
        assertThat(countRowsInTableWhere(FILMS, "vrijePlaatsen = 0 and id = " + id)).isOne();
    }

    @Test
    void reserveerVoorOnbestaandeFilmMislukt() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteReservatie.json"));
        mockMvc.perform(post("/films/{id}/reservatie", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isNotFound());
    }

    @Test
    void reserveerTeveelTicketsMislukt() throws Exception {
        var id = findIdTestFilm1();
        var jsonData = Files.readString(TEST_RESOURCES.resolve("reservatieMetTeveelTickets.json"));
        mockMvc.perform(post("/films/{id}/reservatie", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isConflict());
    }
    @ParameterizedTest
    @ValueSource(strings = { "reservatieMetFoutiefEmailAdres.json", "reservatieMetNegatiefAantalTickets.json",
    "reservatieZonderEmailAdres.json", "reservatieZonderTickets.json" })
    void reserveerMetFoutieveDataMislukt(String fileName) throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve(fileName));
        mockMvc.perform(post("/films/{id}/reservatie", findIdTestFilm1())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }
}
