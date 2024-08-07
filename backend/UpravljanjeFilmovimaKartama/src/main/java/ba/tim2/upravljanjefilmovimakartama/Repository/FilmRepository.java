package ba.tim2.upravljanjefilmovimakartama.Repository;

import ba.tim2.upravljanjefilmovimakartama.Entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    //Film findByName(String name);
    Film findByID(int id);

    @Query("SELECT f FROM Film f WHERE f.nazivFilma  = :naziv_filma")
    Film postojiLiFilm(@Param("naziv_filma") String nazivFilma);

    @Query("SELECT COUNT(f) > 0 FROM Film f WHERE f.nazivFilma = :naziv_filma")
    boolean postojiLiFilm1(@Param("naziv_filma") String nazivFilma);

    Film findFirstByNazivFilma(String nazivFilma);

    Film findTopByNazivFilmaOrderByIDDesc(String nazivFilma);
}
