package be.vdab.cinefest.controllers;

import be.vdab.cinefest.dto.ReservatieMetFilmTitel;
import be.vdab.cinefest.services.ReservatieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class ReservatieController {
    private final ReservatieService reservatieService;

    public ReservatieController(ReservatieService reservatieService) {
        this.reservatieService = reservatieService;
    }
    @GetMapping(value = "reservaties", params = "emailAdres")
    List<ReservatieMetFilmTitel> findByEmailAdres(String emailAdres) {
        return reservatieService.findByEmailAdres(emailAdres);
    }
}
