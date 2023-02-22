package be.vdab.cinefest.controllers;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.domain.Reservatie;
import be.vdab.cinefest.dto.NieuweFilm;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.services.FilmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("films")
class FilmController {
    private final FilmService filmService;
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    private record IdTitelJaarVrijePlaatsen(long id, String titel, int jaar, int vrijePlaatsen) {
        IdTitelJaarVrijePlaatsen(Film film) {
            this(film.getId(), film.getTitel(), film.getJaar(), film.getVrijePlaatsen());
        }
    }
    private record NieuweTitel(@NotBlank String titel) {}
    private record NieuweReservatie(@NotNull @Email String emailAdres, @Positive int aantalTickets) {}
    @GetMapping("totaalvrijeplaatsen")
    long findTotaalVrijePlaatsen() {
        return filmService.findTotaalVrijePlaatsen();
    }
    @GetMapping("{id}")
    IdTitelJaarVrijePlaatsen findById(@PathVariable long id) {
        return filmService.findById(id)
                .map(film -> new IdTitelJaarVrijePlaatsen(film))
                .orElseThrow(() -> new FilmNietGevondenException(id));
    }
    @GetMapping
    Stream<IdTitelJaarVrijePlaatsen> findAll() {
        return filmService.findAll()
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @GetMapping(params = "jaar")
    Stream<IdTitelJaarVrijePlaatsen> findByJaar(int jaar) {
        return filmService.findByJaar(jaar)
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        filmService.delete(id);
    }
    @PostMapping
    long create(@RequestBody @Valid NieuweFilm nieuweFilm) {
        return filmService.create(nieuweFilm);
    }
    @PatchMapping("{id}/titel")
    void updateTitel(@PathVariable long id,
                     @RequestBody @Valid NieuweTitel nieuweTitel) {
        filmService.updateTitel(id, nieuweTitel.titel);
    }
    @PostMapping("{id}/reservatie")
    long reserveer(@PathVariable long id,
                   @RequestBody @Valid NieuweReservatie reservatie) {
        return filmService.reserveer(new Reservatie(id, reservatie.emailAdres(), reservatie.aantalTickets()));
    }
}
