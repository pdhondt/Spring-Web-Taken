package be.vdab.cinefest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record NieuweFilm(@NotBlank String titel, @Positive int jaar) {
}
