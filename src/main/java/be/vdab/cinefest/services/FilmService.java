package be.vdab.cinefest.services;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.dto.NieuweFilm;
import be.vdab.cinefest.repositories.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }
    public long findTotaalVrijePlaatsen() {
        return filmRepository.findTotaalVrijePlaatsen();
    }
    public Optional<Film> findById(long id) {
        return filmRepository.findById(id);
    }
    public List<Film> findAll() {
        return filmRepository.findAll();
    }
    public List<Film> findByJaar(int jaar) {
        return filmRepository.findByJaar(jaar);
    }
    @Transactional
    public void delete(long id) {
        filmRepository.delete(id);
    }
    @Transactional
    public long create(NieuweFilm nieuweFilm) {
        return filmRepository.create(new Film(nieuweFilm.titel(), nieuweFilm.jaar(), 0, BigDecimal.ZERO));
    }
    @Transactional
    public void updateTitel(long id, String titel) {
        filmRepository.updateTitel(id, titel);
    }
}
