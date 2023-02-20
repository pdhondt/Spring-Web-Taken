package be.vdab.cinefest.domain;

import be.vdab.cinefest.exceptions.OnvoldoendeVrijePlaatsenException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FilmTest {
    @Test
    void verminderVrijePlaatsen() {
        var testFilm = new Film("testfilm", 2023, 2, BigDecimal.ONE);
        testFilm.verminderVrijePlaatsen(1);
        assertThat(testFilm.getVrijePlaatsen()).isEqualTo(1);
    }
    @Test
    void verminderVrijePlaatsenMetOnvoldoendeVrijPlaatsenMislukt() {
        var testFilm = new Film("testfilm", 2023, 2, BigDecimal.ONE);
        assertThatExceptionOfType(OnvoldoendeVrijePlaatsenException.class)
                .isThrownBy(() -> testFilm.verminderVrijePlaatsen(3));
    }
}
