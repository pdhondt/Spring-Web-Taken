package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository {
    private final JdbcTemplate template;
    private final RowMapper<Film> filmMapper = (rs, rowNum) ->
            new Film(rs.getLong("id"), rs.getString("titel"),
                    rs.getInt("jaar"), rs.getInt("vrijePlaatsen"),
                    rs.getBigDecimal("aankoopprijs"));

    public FilmRepository(JdbcTemplate template) {
        this.template = template;
    }

    public long findTotaalVrijePlaatsen() {
        var sql = """
                select sum(vrijePlaatsen) as totaalVrijePlaatsen
                from films
                """;
        return template.queryForObject(sql, Long.class);
    }
    public Optional<Film> findById(long id) {
        try {
            var sql = """
                    select id, titel, jaar, vrijePlaatsen, aankoopprijs
                    from films
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, filmMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
    public List<Film> findAll() {
        var sql = """
                select id, titel, jaar, vrijePlaatsen, aankoopprijs
                from films
                order by titel
                """;
        return template.query(sql, filmMapper);
    }
    public List<Film> findByJaar(int jaar) {
        var sql = """
                select id, titel, jaar, vrijePlaatsen, aankoopprijs
                from films
                where jaar = ?
                order by titel
                """;
        return template.query(sql, filmMapper, jaar);
    }
    public void delete(long id) {
        var sql = """
                delete from films
                where id = ?
                """;
        template.update(sql, id);
    }
    public long create(Film film) {
        var sql = """
                
                insert into films(titel, jaar, vrijePlaatsen, aankoopprijs)
                values (?, ?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getTitel());
            statement.setInt(2, film.getJaar());
            statement.setInt(3, film.getVrijePlaatsen());
            statement.setBigDecimal(4, film.getAankoopprijs());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    public void updateTitel(long id, String titel) {
        var sql = """
                update films
                set titel = ?
                where id = ?
                """;
        if (template.update(sql, titel, id) == 0) {
            throw new FilmNietGevondenException(id);
        }
    }
    public Optional<Film> findAndLockById(long id) {
        try {
            var sql = """
                    select id, titel, jaar, vrijePlaatsen, aankoopprijs
                    from films
                    where id = ?
                    for update
                    """;
            return Optional.of(template.queryForObject(sql, filmMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
    public void updateVrijePlaatsen(long id, int vrijePlaatsen) {
        var sql = """
                update films
                set vrijePlaatsen = ?
                where id = ?
                """;
        if (template.update(sql, vrijePlaatsen, id) == 0) {
            throw new FilmNietGevondenException(id);
        }
    }
}
