package be.vdab.cinefest.domain;

import be.vdab.cinefest.exceptions.OnvoldoendeVrijePlaatsenException;

import java.math.BigDecimal;

public class Film {
    private final long id;
    private final String titel;
    private final int jaar;
    private int vrijePlaatsen;
    private final BigDecimal aankoopprijs;

    public Film(long id, String titel, int jaar, int vrijePlaatsen, BigDecimal aankoopprijs) {
        this.id = id;
        this.titel = titel;
        this.jaar = jaar;
        this.vrijePlaatsen = vrijePlaatsen;
        this.aankoopprijs = aankoopprijs;
    }

    public Film(String titel, int jaar, int vrijePlaatsen, BigDecimal aankoopprijs) {
        this(0, titel, jaar, vrijePlaatsen, aankoopprijs);
    }

    public long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public int getJaar() {
        return jaar;
    }

    public int getVrijePlaatsen() {
        return vrijePlaatsen;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }
    public void verminderVrijePlaatsen(int aantalTickets) {
        if (aantalTickets > this.vrijePlaatsen) {
            throw new OnvoldoendeVrijePlaatsenException(this.vrijePlaatsen);
        }
        this.vrijePlaatsen -= aantalTickets;
    }
}
