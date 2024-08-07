package ba.tim2.upravljanjefilmovimakartama.Service;

import ba.tim2.upravljanjefilmovimakartama.Entity.Film;
import ba.tim2.upravljanjefilmovimakartama.Entity.Sala;
import ba.tim2.upravljanjefilmovimakartama.ErrorHandling.NePostojiException;
import ba.tim2.upravljanjefilmovimakartama.Repository.FilmRepository;
import ba.tim2.upravljanjefilmovimakartama.Repository.SalaRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaServiceImpl implements SalaService {
    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private FilmRepository filmRepository;




    public ResponseEntity getSaluPrekoBrojaSale(int brojSale) {       //OVO vidi za if
        return new ResponseEntity(salaRepository.findBrojSalePrekoSale(brojSale), HttpStatus.OK);
    }

    public ResponseEntity spasiSalu(Sala sala) {
        salaRepository.save(sala);

        JSONObject objekat = new JSONObject();
        try {
            objekat.put("message", "Sala je uspješno dodana!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sala> request = new HttpEntity<>(sala, headers);
        //restTemplate.postForObject("http:://localhost:8080/dodajFilm", request, Film.class);
        return new ResponseEntity(sala, HttpStatus.CREATED);
    }

//    public List<Sala> spasiSale(List<Sala> filmovi) {
//        return repository.saveAll(filmovi);
//    }

    public List<Sala> getSveSale() {
        return salaRepository.findAll();
    }

    public ResponseEntity obrisiSveSale() {
        if(salaRepository.count() > 0) {
            JSONObject objekat = new JSONObject();
            salaRepository.deleteAll();
            try {
                objekat.put("message", "Sale su uspješno obrisana!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //restTemplate.delete("http://localhost:8080/obrisiFilm" + id);
            return new ResponseEntity(objekat.toString(), HttpStatus.OK);
        } else {
            throw new NePostojiException("Nema sala dostupnih za brisanje");
        }
    }

    public ResponseEntity obrisiSalu(int id) {
        if (salaRepository.existsById(id)) {
            JSONObject objekat = new JSONObject();
            salaRepository.deleteById(id);
            try {
                objekat.put("message", "Sala je uspješno obrisana!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //restTemplate.delete("http://localhost:8080/obrisiFilm" + id);
            return new ResponseEntity(objekat.toString(), HttpStatus.OK);
        } else {
            throw new NePostojiException("Sala sa id-em " + id + " ne postoji!");
        }
    }

    @Override
    public ResponseEntity postaviFilmZaSalu(int id, List<Sala> saleZaFilm) {
        Film f = filmRepository.findByID(id);
        if (f != null){
            //GrpcClient.log("Sala", "PUT sale/film/{id}", "SUCCESS");
            for (int i = 0; i < saleZaFilm.size(); i++) {
                int index = saleZaFilm.get(i).getID();
                if(salaRepository.findByID(index) != null){
                    f.dodajSalu(salaRepository.findByID(index));
                    Sala sala = salaRepository.findByID(index);
                    sala.dodajFilm(f);
                    salaRepository.save(sala);
                }
            }
            filmRepository.save(f);
        }
        else {
            //GrpcClient.log("Sala", "PUT sale/film/{id}", "FAIL");
            throw new NePostojiException("Film sa id-em " + id + " ne postoji!");
        }
        return new ResponseEntity(f, HttpStatus.OK);
    }
}
