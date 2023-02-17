package be.vdab.cinefest.controllers;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.dto.NieuweFilm;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.services.FilmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
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
    @GetMapping("films/totaalvrijeplaatsen")
    long findTotaalVrijePlaatsen() {
        return filmService.findTotaalVrijePlaatsen();
    }
    @GetMapping("films/{id}")
    IdTitelJaarVrijePlaatsen findById(@PathVariable long id) {
        return filmService.findById(id)
                .map(film -> new IdTitelJaarVrijePlaatsen(film))
                .orElseThrow(() -> new FilmNietGevondenException(id));
    }
    @GetMapping("films")
    Stream<IdTitelJaarVrijePlaatsen> findAll() {
        return filmService.findAll()
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @GetMapping(value = "films", params = "jaar")
    Stream<IdTitelJaarVrijePlaatsen> findByJaar(int jaar) {
        return filmService.findByJaar(jaar)
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @DeleteMapping("films/{id}")
    void delete(@PathVariable long id) {
        filmService.delete(id);
    }
    @PostMapping("films")
    long create(@RequestBody @Valid NieuweFilm nieuweFilm) {
        return filmService.create(nieuweFilm);
    }
    @PatchMapping("films/{id}/titel")
    void updateTitel(@PathVariable long id,
                     @RequestBody @Valid NieuweTitel nieuweTitel) {
        filmService.updateTitel(id, nieuweTitel.titel);
    }
}
