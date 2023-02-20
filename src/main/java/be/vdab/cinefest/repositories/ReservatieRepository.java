package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Reservatie;
import be.vdab.cinefest.dto.ReservatieMetFilmTitel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservatieRepository {
    private final JdbcTemplate template;
    private final RowMapper<ReservatieMetFilmTitel> reservatieMetFilmTitelMapper = (rs, rowNum) ->
            new ReservatieMetFilmTitel(rs.getLong("id"), rs.getString("titel"),
                    rs.getInt("plaatsen"), rs.getObject("besteld", LocalDateTime.class));

    public ReservatieRepository(JdbcTemplate template) {
        this.template = template;
    }
    public long create(Reservatie reservatie) {
        var sql = """
                insert into reservaties(filmId, emailAdres, plaatsen, besteld)
                values (?, ?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, reservatie.getFilmId());
            statement.setString(2, reservatie.getEmailAdres());
            statement.setInt(3, reservatie.getPlaatsen());
            statement.setObject(4, reservatie.getBesteld());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    public List<ReservatieMetFilmTitel> findByEmailAdres(String emailAdres) {
        var sql = """
                select reservaties.id, titel, plaatsen, besteld
                from reservaties inner join films
                on reservaties.filmId = films.id
                where emailAdres = ?
                order by reservaties.id desc
                """;
        return template.query(sql, reservatieMetFilmTitelMapper, emailAdres);
    }
}
