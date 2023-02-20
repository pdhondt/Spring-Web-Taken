package be.vdab.cinefest.dto;

import java.time.LocalDateTime;

public record ReservatieMetFilmTitel(long id, String titel, int plaatsen, LocalDateTime besteld) {
}
