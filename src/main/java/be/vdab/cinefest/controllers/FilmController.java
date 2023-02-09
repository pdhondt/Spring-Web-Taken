package be.vdab.cinefest.controllers;

import be.vdab.cinefest.services.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FilmController {
    private final FilmService filmService;
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping("films/totaalvrijeplaatsen")
    long findTotaalVrijePlaatsen() {
        return filmService.findTotaalVrijePlaatsen();
    }
}
