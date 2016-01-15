//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import sgdi.pr3.grupo03.shared.MongoUtil;
import sgdi.pr3.grupo03.situacion2.model.*;

public class TestData {
    public static void execute() {
        try {
            insertUsers();
            insertFilms();
            insertSeries();
            insertMovieTheaters();
            insertUserWatchedWorks();
            MongoUtil.close();
            System.out.println("OK");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private static final int MAX = 10;
    private static List<ObjectId> userIds = null;

    private static void insertUsers() {
        userIds = new ArrayList<>();

        for (int i = 0; i < MAX; i++) {
            User uvictim = new User();
            uvictim.name = "Usuario " + i;
            uvictim.birthDate = new Date();
            uvictim.birthDate.day = 1;
            uvictim.birthDate.month = 1;
            uvictim.birthDate.year = 1970 + i;
            uvictim.email = "usuario@ucm.es";
            uvictim.episodeIdRefs = new ArrayList<>();
            uvictim.films = new ArrayList<>();
            DBHelper.insertUser(uvictim);
            
            uvictim = DBHelper.getUserByName(uvictim.name);
            userIds.add(uvictim._id);
        }
    }

    private static void insertUserWatchedWorks() {
        for (int i = 0; i < MAX; i++) {
            User uvictim = DBHelper.getUserByName("Usuario " + i);
            MovieTheater mvictim = DBHelper.getMovieTheaterByName("Cine " + i);
            if (uvictim.films == null) {
                uvictim.films = new ArrayList<>();
            }
            for (Auditorium auditorium : mvictim.auditoriums) {
                WatchedFilm wfvictim = new WatchedFilm();
                wfvictim.movieTheaterIdRef = mvictim._id;
                wfvictim.filmIdRef = auditorium.filmIdRef;
                wfvictim.auditorium = auditorium.number;
                uvictim.films.add(wfvictim);
            }
            if (uvictim.episodeIdRefs == null) {
                uvictim.episodeIdRefs = new ArrayList<>();
            }
            for (int j = 0; j < 5; j++) {
                Episode evictim = DBHelper.getEpisodeByTitle("Serie " + i + " T" + j + "E1");
                uvictim.episodeIdRefs.add(evictim._id);
            }
            DBHelper.updateUser(uvictim);
        }
    }

    private static void insertMovieTheaters() {
        for (int i = 0; i < MAX; i++) {
            MovieTheater mvictim = new MovieTheater();
            mvictim.name = "Cine " + i;
            mvictim.address = "Dirección " + 1;
            mvictim.auditoriums = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                int k = (j + i) % MAX;
                Film fvictim = DBHelper.getFilmByTitle("Película " + k);
                Auditorium avictim = new Auditorium();
                avictim.filmIdRef = fvictim._id;
                avictim.number = j + 1;
                avictim.capacity = 80;
                mvictim.auditoriums.add(avictim);
            }
            DBHelper.insertMovieTheater(mvictim);
        }
    }

    private static void insertFilms() {
        Valuation vb = new Valuation();
        vb.title = "Valoración ";
        vb.punctuation = 0;
        vb.explanation = "Comentario ";

        Film fb = new Film();
        fb.title = "Película ";
        fb.releaseYear = 0;
        fb.directors = new ArrayList<>();
        fb.directors.add("Director A");
        fb.directors.add("Director B");
        fb.countriesWhereFilmed = new ArrayList<>();
        fb.countriesWhereFilmed.add("D");
        fb.countriesWhereFilmed.add("E");
        fb.actors = new ArrayList<>();
        ActorWithCharacter[] actors = new ActorWithCharacter[2];
        actors[0] = new ActorWithCharacter();
        actors[0].actor = "Actor A";
        actors[0].character = "Personaje A";
        actors[1] = new ActorWithCharacter();
        actors[1].actor = "Actor B";
        actors[1].character = "Personaje B";
        for (ActorWithCharacter actor : actors) {
            fb.actors.add(actor);
        }
        fb.synopsis = "Sinopsis ";
        fb.writers = new ArrayList<>();
        fb.writers.add("Escritor A");
        fb.writers.add("Escritor B");

        for (int i = 0; i < MAX; i++) {
            int l = i % 5;
            Film fvictim = new Film();
            fvictim.title = fb.title + i;
            fvictim.releaseYear = fb.releaseYear + i;
            fvictim.directors = new ArrayList<>();
            for (String director : fb.directors) {
                fvictim.directors.add(director + l);
            }
            fvictim.countriesWhereFilmed = new ArrayList<>();
            for (String countriesWhereFilmed : fb.countriesWhereFilmed) {
                fvictim.countriesWhereFilmed.add(countriesWhereFilmed + l);
            }
            fvictim.actors = new ArrayList<>();
            for (ActorWithCharacter actor : actors) {
                ActorWithCharacter avictim = new ActorWithCharacter();
                avictim.actor = actor.actor + l;
                avictim.character = actor.character + l;
                fvictim.actors.add(avictim);
            }
            fvictim.synopsis = fb.synopsis + i;
            fvictim.writers = new ArrayList<>();
            for (String writer : fb.writers) {
                fvictim.writers.add(writer + l);
            }
            DBHelper.insertFilm(fvictim);

            String title = fb.title + i;
            Valuation vvictim = new Valuation();
            fvictim = DBHelper.getFilmByTitle(title);
            vvictim.userIdRef = userIds.get(i);
            vvictim.idRef = fvictim._id;
            vvictim.title = vb.title + title;
            vvictim.punctuation = vb.punctuation + i;
            vvictim.explanation = vb.explanation + title;
            DBHelper.insertValuation(vvictim);
        }
    }
    
    private static void insertSeries() {
        Valuation vb = new Valuation();
        vb.title = "Valoración ";
        vb.punctuation = 0;
        vb.explanation = "Comentario ";

        Series s1b = new Series();
        s1b.title = "Serie ";
        s1b.releaseYear = 0;
        s1b.synopsis = "Sinopsis ";
        
        Season s2b = new Season();
        s2b.synopsis = "Sinopsis ";
        s2b.releaseYear = 0;
        
        Episode eb = new Episode();
        eb.title = "Episodio ";
        eb.synopsis = "Sinopsis ";
        eb.releaseDate = new Date();
        eb.releaseDate.day = 1;
        eb.releaseDate.month = 1;
        eb.releaseDate.year = 0;
        eb.actors = new ArrayList<>();
        ActorWithCharacter[] actors = new ActorWithCharacter[2];
        actors[0] = new ActorWithCharacter();
        actors[0].actor = "Actor A";
        actors[0].character = "Personaje A";
        actors[1] = new ActorWithCharacter();
        actors[1].actor = "Actor B";
        actors[1].character = "Personaje B";
        for (ActorWithCharacter actor : actors) {
            eb.actors.add(actor);
        }
        eb.writers = new ArrayList<>();
        eb.writers.add("Escritor A");
        eb.writers.add("Escritor B");
        eb.directors = new ArrayList<>();
        eb.directors.add("Director A");
        eb.directors.add("Director B");

        for (int i = 0; i < MAX; i++) {
            Valuation vvictim;
            String title = s1b.title + i;

            Series s1victim = new Series();
            s1victim.title = title;
            s1victim.releaseYear = s1b.releaseYear + i;
            s1victim.synopsis = s1b.synopsis + i;
            DBHelper.insertSeries(s1victim);

            vvictim = new Valuation();
            s1victim = DBHelper.getOneSeriesByTitle(title);
            vvictim.userIdRef = userIds.get(i);
            vvictim.idRef = s1victim._id;
            vvictim.title = vb.title + title;
            vvictim.punctuation = vb.punctuation + i;
            vvictim.explanation = vb.explanation + title;
            DBHelper.insertValuation(vvictim);

            for (int j = 0; j < MAX; j++) {
                Season s2victim = new Season();

                s2victim.seriesIdRef = s1victim._id;
                s2victim.synopsis = s1victim.title + " " + s2b.synopsis + j;
                s2victim.releaseYear = j;
                DBHelper.insertSeason(s2victim);

                String title2 = title + " T" + s2victim.releaseYear;
                vvictim = new Valuation();
                s2victim = DBHelper.getSeasonByYear(title, s2victim.releaseYear);
                vvictim.userIdRef = userIds.get(i);
                vvictim.idRef = s2victim._id;
                vvictim.title = vb.title + title2;
                vvictim.punctuation = vb.punctuation + j;
                vvictim.explanation = vb.explanation + title2;
                DBHelper.insertValuation(vvictim);

                for (int k = 0; k < MAX; k++) {
                    int l = k % 5;
                    String title3 = title2 + "E" + k;
                    Episode evictim = new Episode();
                    evictim.seasonIdRef = s2victim._id;
                    evictim.title = title3;
                    evictim.synopsis = eb.synopsis + k;
                    evictim.releaseDate = new Date();
                    evictim.releaseDate.day = eb.releaseDate.day;
                    evictim.releaseDate.month = eb.releaseDate.month + k;
                    evictim.releaseDate.year = eb.releaseDate.year + j;
                    evictim.actors = new ArrayList<>();
                    for (ActorWithCharacter actor : actors) {
                        ActorWithCharacter avictim = new ActorWithCharacter();
                        avictim.actor = actor.actor + l;
                        avictim.character = actor.character + l;
                        evictim.actors.add(avictim);
                    }
                    evictim.writers = new ArrayList<>();
                    for (String writer : eb.writers) {
                        evictim.writers.add(writer + l);
                    }
                    evictim.directors = new ArrayList<>();
                    for (String director : eb.directors) {
                        evictim.directors.add(director + l);
                    }
                    DBHelper.insertEpisode(evictim);

                    vvictim = new Valuation();
                    evictim = DBHelper.getEpisodeByTitle(s1victim.title,
                            s2victim.releaseYear, evictim.title);
                    vvictim.userIdRef = userIds.get(i);
                    vvictim.idRef = evictim._id;
                    vvictim.title = vb.title + title3;
                    vvictim.punctuation = vb.punctuation + k;
                    vvictim.explanation = vb.explanation + title3;
                    DBHelper.insertValuation(vvictim);
                }
            }
        }
    }
}
