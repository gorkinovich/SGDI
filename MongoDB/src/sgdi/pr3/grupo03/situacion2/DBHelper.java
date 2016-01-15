//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import sgdi.pr3.grupo03.shared.CustomFind;
import sgdi.pr3.grupo03.shared.CustomFindOne;
import sgdi.pr3.grupo03.shared.MongoSession;
import sgdi.pr3.grupo03.shared.MongoUtil;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.User;
import sgdi.pr3.grupo03.situacion2.model.Valuation;

public class DBHelper {
    // ======================================================================================
    // Elementos del apartado A:
    // ======================================================================================

    private static final String DB_NAME = "sgdi_pr3_grupo3_situacion2";
    private static final String FILMS_COLLECTION_NAME = "films";
    private static final String SERIES_COLLECTION_NAME = "series";
    private static final String SEASONS_COLLECTION_NAME = "seasons";
    private static final String EPISODES_COLLECTION_NAME = "episodes";
    private static final String VALUATIONS_COLLECTION_NAME = "valuations";

    // --------------------------------------------------------------------------------------
    // Insertar/modificar/borrar una película:
    // --------------------------------------------------------------------------------------

    public static boolean insertFilm(Film victim) {
        return MongoUtil.insert(DB_NAME, FILMS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateFilm(Film victim) {
        return MongoUtil.updateById(DB_NAME, FILMS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeFilm(Film victim) {
        return MongoUtil.removeById(DB_NAME, FILMS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static Film getFilmByTitle(String title) {
        DBObject victim = MongoUtil.findOne(DB_NAME, FILMS_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toUserObject(Film.class, victim);
    }

    public static List<Film> getFilmsByTitle(String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, FILMS_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toList(Film.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Insertar/modificar/borrar una serie de TV:
    // --------------------------------------------------------------------------------------

    public static boolean insertSeries(Series victim) {
        return MongoUtil.insert(DB_NAME, SERIES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateSeries(Series victim) {
        return MongoUtil.updateById(DB_NAME, SERIES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeSeries(Series victim) {
        return MongoUtil.removeById(DB_NAME, SERIES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static Series getOneSeriesByTitle(String title) {
        DBObject victim = MongoUtil.findOne(DB_NAME, SERIES_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toUserObject(Series.class, victim);
    }

    public static List<Series> getSeriesByTitle(String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, SERIES_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toList(Series.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Insertar/modificar/borrar una temporada de una serie de TV:
    // --------------------------------------------------------------------------------------

    public static boolean insertSeason(Season victim) {
        return MongoUtil.insert(DB_NAME, SEASONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateSeason(Season victim) {
        return MongoUtil.updateById(DB_NAME, SEASONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeSeason(Season victim) {
        return MongoUtil.removeById(DB_NAME, SEASONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static Season getSeasonByYear(ObjectId id, int year) {
        DBObject query = new BasicDBObject("seriesIdRef", id).append(
                "releaseYear", year);
        DBObject victim = MongoUtil.findOne(DB_NAME, SEASONS_COLLECTION_NAME,
                query);
        return MongoUtil.toUserObject(Season.class, victim);
    }

    public static Season getSeasonByYear(final String title, final int year) {
        DBObject victim = MongoUtil.findOne(DB_NAME, new CustomFindOne() {
            @Override
            public DBObject execute(MongoSession ms) {
                DBCollection col1 = ms.getCollection(SERIES_COLLECTION_NAME);
                DBObject series = col1
                        .findOne(new BasicDBObject("title", title));
                if (series == null)
                    return null;
                DBObject query = new BasicDBObject("seriesIdRef", series
                        .get("_id")).append("releaseYear", year);
                DBCollection col2 = ms.getCollection(SEASONS_COLLECTION_NAME);
                return col2.findOne(query);
            }
        });
        return MongoUtil.toUserObject(Season.class, victim);
    }

    public static List<Season> getSeasonsByYear(int year) {
        DBCursor victims = MongoUtil.find(DB_NAME, SEASONS_COLLECTION_NAME,
                "releaseYear", year);
        return MongoUtil.toList(Season.class, victims);
    }

    public static List<Season> getSeasonsByYear(final String title,
            final int year) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col1 = ms.getCollection(SERIES_COLLECTION_NAME);
                DBCursor series = col1.find(new BasicDBObject("title", title));
                if (series == null)
                    return null;
                BasicDBList list = new BasicDBList();
                for (DBObject current : series) {
                    list.add(current.get("_id"));
                }
                DBObject query = new BasicDBObject("releaseYear", year).append(
                        "seriesIdRef", new BasicDBObject("$in", list));
                DBCollection col2 = ms.getCollection(SEASONS_COLLECTION_NAME);
                return col2.find(query);
            }
        });
        return MongoUtil.toList(Season.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Insertar/modificar/borrar un episodio de una temporada de una serie de
    // TV:
    // --------------------------------------------------------------------------------------

    public static boolean insertEpisode(Episode victim) {
        return MongoUtil.insert(DB_NAME, EPISODES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateEpisode(Episode victim) {
        return MongoUtil.updateById(DB_NAME, EPISODES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeEpisode(Episode victim) {
        return MongoUtil.removeById(DB_NAME, EPISODES_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static Episode getEpisodeByTitle(String title) {
        DBObject victim = MongoUtil.findOne(DB_NAME, EPISODES_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toUserObject(Episode.class, victim);
    }

    public static List<Episode> getEpisodesByTitle(String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, EPISODES_COLLECTION_NAME,
                "title", title);
        return MongoUtil.toList(Episode.class, victims);
    }

    public static Episode getEpisodeByTitle(final String stitle, final int year,
            final String title) {
        DBObject victim = MongoUtil.findOne(DB_NAME, new CustomFindOne() {
            @Override
            public DBObject execute(MongoSession ms) {
                DBCollection col1 = ms.getCollection(SERIES_COLLECTION_NAME);
                DBObject series = col1.findOne(new BasicDBObject("title",
                        stitle));
                if (series == null)
                    return null;
                DBObject query = new BasicDBObject("seriesIdRef", series
                        .get("_id")).append("releaseYear", year);
                DBCollection col2 = ms.getCollection(SEASONS_COLLECTION_NAME);
                DBObject season = col2.findOne(query);
                if (season == null)
                    return null;
                DBCollection col3 = ms.getCollection(EPISODES_COLLECTION_NAME);
                query = new BasicDBObject("seasonIdRef", season.get("_id"))
                        .append("title", title);
                return col3.findOne(query);
            }
        });
        return MongoUtil.toUserObject(Episode.class, victim);
    }

    // --------------------------------------------------------------------------------------
    // Añadir una valoración a una película, serie, temporada o episodio:
    // --------------------------------------------------------------------------------------

    public static boolean insertValuation(Valuation victim) {
        return MongoUtil.insert(DB_NAME, VALUATIONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateValuation(Valuation victim) {
        return MongoUtil.updateById(DB_NAME, VALUATIONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeValuation(Valuation victim) {
        return MongoUtil.removeById(DB_NAME, VALUATIONS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static Valuation getValuationByTitle(ObjectId id, String title) {
        DBObject query = new BasicDBObject("idRef", id).append("title", title);
        DBObject victim = MongoUtil.findOne(DB_NAME,
                VALUATIONS_COLLECTION_NAME, query);
        return MongoUtil.toUserObject(Valuation.class, victim);
    }

    // --------------------------------------------------------------------------------------
    // Conocer el nombre de los actores que aparecen en una película o serie de
    // TV
    // (se proporciona el título):
    // --------------------------------------------------------------------------------------

    public static List<String> getActorsByTitle(final String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBObject filter1 = new BasicDBObject("_id", 1);
                DBObject filter2 = new BasicDBObject("_id", 0).append("actors",
                        1);
                DBObject query = new BasicDBObject("title", title);
                DBCollection col = ms.getCollection(FILMS_COLLECTION_NAME);
                DBCursor films = col.find(query, filter2);
                if (films.hasNext()) {
                    return films;
                }

                col = ms.getCollection(SERIES_COLLECTION_NAME);
                DBCursor series = col.find(query, filter1);
                BasicDBList list = new BasicDBList();
                for (DBObject current : series) {
                    list.add(current.get("_id"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(SEASONS_COLLECTION_NAME);
                query = new BasicDBObject("seriesIdRef", new BasicDBObject(
                        "$in", list));
                DBCursor seasons = col.find(query, filter1);
                list = new BasicDBList();
                for (DBObject current : seasons) {
                    list.add(current.get("_id"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(EPISODES_COLLECTION_NAME);
                query = new BasicDBObject("seasonIdRef", new BasicDBObject(
                        "$in", list));
                return col.find(query, filter2);
            }
        });
        List<String> actors = new ArrayList<>();
        if (victims != null) {
            for (DBObject current : victims) {
                for (Object item : (List) current.get("actors")) {
                    DBObject actcur = (DBObject) item;
                    actors.add((String) actcur.get("actor"));
                }
            }
        }
        return actors;
    }

    // --------------------------------------------------------------------------------------
    // Conocer el título de las películas o series de TV en las que aparece un
    // determinado
    // actor (se proporciona el nombre).:
    // --------------------------------------------------------------------------------------

    public static List<String> getWorksByActor(String actor) {
        List<String> works = new ArrayList<>();

        final DBObject query = new BasicDBObject("actors.actor", actor);
        DBCursor victims = MongoUtil
                .find(DB_NAME, FILMS_COLLECTION_NAME, query);
        for (DBObject current : victims) {
            works.add((String) current.get("title"));
        }

        victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms.getCollection(EPISODES_COLLECTION_NAME);
                DBCursor episodes = col.find(query);

                BasicDBList list = new BasicDBList();
                for (DBObject current : episodes) {
                    list.add(current.get("seasonIdRef"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(SEASONS_COLLECTION_NAME);
                DBCursor seasons = col.find(new BasicDBObject("_id",
                        new BasicDBObject("$in", list)));

                list = new BasicDBList();
                for (DBObject current : seasons) {
                    list.add(current.get("seriesIdRef"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(SERIES_COLLECTION_NAME);
                return col.find(new BasicDBObject("_id", new BasicDBObject(
                        "$in", list)));
            }
        });
        if (victims != null) {
            for (DBObject current : victims) {
                works.add((String) current.get("title"));
            }
        }

        return works;
    }

    // --------------------------------------------------------------------------------------
    // Dado el nombre de un actor y el título de una película o episodio de
    // serie de TV,
    // encontrar el personaje que interpreta:
    // --------------------------------------------------------------------------------------

    public static String getCharacterByActorAndTitle(final String actor,
            final String title) {
        DBObject victim = MongoUtil.findOne(DB_NAME, new CustomFindOne() {
            @Override
            public DBObject execute(MongoSession ms) {
                DBObject filter = new BasicDBObject("_id", 0).append("actors",
                        1);
                DBObject query = new BasicDBObject("title", title).append(
                        "actors.actor", actor);
                DBCollection col = ms.getCollection(FILMS_COLLECTION_NAME);
                DBObject film = col.findOne(query, filter);
                if (film != null)
                    return film;

                col = ms.getCollection(EPISODES_COLLECTION_NAME);
                return col.findOne(query, filter);
            }
        });
        if (victim == null)
            return null;
        List actors = (List) victim.get("actors");
        for (Object item : actors) {
            DBObject current = (DBObject)item;
            String name = (String) current.get("actor");
            if (name.equals(actor)) {
                return (String) current.get("character");
            }
        }
        return null;
    }

    // --------------------------------------------------------------------------------------
    // Consultar las películas grabadas en un determinado país:
    // --------------------------------------------------------------------------------------

    public static List<Film> getFilmsByCountry(String country) {
        DBCursor victims = MongoUtil.find(DB_NAME, FILMS_COLLECTION_NAME,
                "countriesWhereFilmed", country);
        return MongoUtil.toList(Film.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Conocer todas las valoraciones para una película, serie, temporada o
    // episodio:
    // --------------------------------------------------------------------------------------

    public static List<Valuation> getValuationsByRefId(ObjectId id) {
        DBCursor victims = MongoUtil.find(DB_NAME, VALUATIONS_COLLECTION_NAME,
                "idRef", id);
        return MongoUtil.toList(Valuation.class, victims);
    }

    // ======================================================================================
    // Elementos del apartado B:
    // ======================================================================================

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String MOVIE_THEATERS_COLLECTION_NAME = "movietheaters";

    // --------------------------------------------------------------------------------------
    // Añadir un usuario:
    // --------------------------------------------------------------------------------------

    public static boolean insertUser(User victim) {
        return MongoUtil.insert(DB_NAME, USERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateUser(User victim) {
        return MongoUtil.updateById(DB_NAME, USERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeUser(User victim) {
        return MongoUtil.removeById(DB_NAME, USERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static User getUserByName(String name) {
        DBObject victim = MongoUtil.findOne(DB_NAME, USERS_COLLECTION_NAME,
                "name", name);
        return MongoUtil.toUserObject(User.class, victim);
    }

    public static List<User> getUsersByName(String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, USERS_COLLECTION_NAME,
                "name", name);
        return MongoUtil.toList(User.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Añadir un cine:
    // --------------------------------------------------------------------------------------

    public static boolean insertMovieTheater(MovieTheater victim) {
        return MongoUtil.insert(DB_NAME, MOVIE_THEATERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, false));
    }

    public static boolean updateMovieTheater(MovieTheater victim) {
        return MongoUtil.updateById(DB_NAME, MOVIE_THEATERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static boolean removeMovieTheater(MovieTheater victim) {
        return MongoUtil.removeById(DB_NAME, MOVIE_THEATERS_COLLECTION_NAME,
                MongoUtil.toDBObject(victim, true));
    }

    public static MovieTheater getMovieTheaterByName(String name) {
        DBObject victim = MongoUtil.findOne(DB_NAME,
                MOVIE_THEATERS_COLLECTION_NAME, "name", name);
        return MongoUtil.toUserObject(MovieTheater.class, victim);
    }

    public static List<MovieTheater> getMovieTheatersByName(String name) {
        DBCursor victims = MongoUtil.find(DB_NAME,
                MOVIE_THEATERS_COLLECTION_NAME, "name", name);
        return MongoUtil.toList(MovieTheater.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Conocer qué usuarios han visto una película o episodio:
    // --------------------------------------------------------------------------------------

    public static List<User> getUsersByTitle(final String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBObject query = new BasicDBObject("title", title);
                DBCollection col = ms.getCollection(FILMS_COLLECTION_NAME);
                DBCursor films = col.find(query);
                col = ms.getCollection(EPISODES_COLLECTION_NAME);
                DBCursor episodes = col.find(query);

                BasicDBList list = new BasicDBList();
                for (DBObject current : films) {
                    list.add(current.get("_id"));
                }
                for (DBObject current : episodes) {
                    list.add(current.get("_id"));
                }
                if (list.size() <= 0)
                    return null;

                DBObject subquery1 = new BasicDBObject("$in", list);
                BasicDBList subquery2 = new BasicDBList();
                subquery2.add(new BasicDBObject("films.filmIdRef", subquery1));
                subquery2.add(new BasicDBObject("episodeIdRefs", subquery1));
                col = ms.getCollection(USERS_COLLECTION_NAME);
                return col.find(new BasicDBObject("$or", subquery2));
            }
        });
        return MongoUtil.toList(User.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Consultar las películas y episodios vistos por un usuario:
    // --------------------------------------------------------------------------------------

    public static List<String> getTitlesByUser(String name) {
        DBObject user = MongoUtil.findOne(DB_NAME, USERS_COLLECTION_NAME,
                "name", name);
        if (user == null)
            return null;

        BasicDBList flist = new BasicDBList();
        BasicDBList elist = (BasicDBList) user.get("episodeIdRefs");
        for (Object item : (BasicDBList) user.get("films")) {
            flist.add(((DBObject) item).get("filmIdRef"));
        }
        if (flist.size() <= 0 && elist.size() <= 0)
            return null;

        DBObject fquery = new BasicDBObject("$in", flist);
        DBObject equery = new BasicDBObject("$in", elist);

        DBCursor films = MongoUtil.find(DB_NAME, FILMS_COLLECTION_NAME,
                new BasicDBObject("_id", fquery));
        DBCursor episodes = MongoUtil.find(DB_NAME, EPISODES_COLLECTION_NAME,
                new BasicDBObject("_id", equery));

        List<String> titles = new ArrayList<>();
        for (DBObject current : films) {
            titles.add((String) current.get("title"));
        }
        for (DBObject current : episodes) {
            titles.add((String) current.get("title"));
        }
        return titles;
    }

    // --------------------------------------------------------------------------------------
    // Ver las valoraciones realizadas por un usuario:
    // --------------------------------------------------------------------------------------

    public static List<Valuation> getValuationsByUser(final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms.getCollection(USERS_COLLECTION_NAME);
                DBObject user = col.findOne(new BasicDBObject("name", name));
                if (user == null)
                    return null;

                col = ms.getCollection(VALUATIONS_COLLECTION_NAME);
                return col.find(new BasicDBObject("userIdRef", user.get("_id")));
            }
        });
        return MongoUtil.toList(Valuation.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Ver aquellas valoraciones muy negativas (puntuación inferior a 4) de un
    // usuario:
    // --------------------------------------------------------------------------------------

    public static List<Valuation> getLowValuationsByUser(final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms.getCollection(USERS_COLLECTION_NAME);
                DBObject user = col.findOne(new BasicDBObject("name", name));
                if (user == null)
                    return null;

                col = ms.getCollection(VALUATIONS_COLLECTION_NAME);
                return col.find(new BasicDBObject("userIdRef", user.get("_id"))
                        .append("punctuation", new BasicDBObject("$lt", 4)));
            }
        });
        return MongoUtil.toList(Valuation.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Ver aquellas valoraciones muy negativas (puntuación inferior a 4) de una
    // película:
    // --------------------------------------------------------------------------------------

    public static List<Valuation> getLowValuationsByFilm(final String title) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms.getCollection(FILMS_COLLECTION_NAME);
                DBObject film = col.findOne(new BasicDBObject("title", title));
                if (film == null)
                    return null;

                col = ms.getCollection(VALUATIONS_COLLECTION_NAME);
                return col.find(new BasicDBObject("idRef", film.get("_id"))
                        .append("punctuation", new BasicDBObject("$lt", 4)));
            }
        });
        return MongoUtil.toList(Valuation.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Consultar la cartelera de un cine dado su nombre:
    // --------------------------------------------------------------------------------------

    public static List<String> getMovieTheaterFilmsByName(final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms
                        .getCollection(MOVIE_THEATERS_COLLECTION_NAME);
                DBObject movieTheater = col.findOne(new BasicDBObject("name",
                        name));
                if (movieTheater == null)
                    return null;

                BasicDBList list = new BasicDBList();
                for (Object item : (BasicDBList) movieTheater
                        .get("auditoriums")) {
                    list.add(((DBObject) item).get("filmIdRef"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(FILMS_COLLECTION_NAME);
                return col.find(new BasicDBObject("_id", new BasicDBObject(
                        "$in", list)));
            }
        });
        List<String> films = new ArrayList<>();
        if (victims != null) {
            for (DBObject current : victims) {
                films.add((String) current.get("title"));
            }
        }
        return films;
    }

    // --------------------------------------------------------------------------------------
    // Consultar las películas (solo su título) que han sido vistas por algún
    // usuario
    // en un cine concreto:
    // --------------------------------------------------------------------------------------

    public static List<String> getWatchedFilmsByMovieTheater(final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms
                        .getCollection(MOVIE_THEATERS_COLLECTION_NAME);
                DBObject movieTheater = col.findOne(new BasicDBObject("name",
                        name));
                if (movieTheater == null)
                    return null;

                Object movieTheaterId = movieTheater.get("_id");
                col = ms.getCollection(USERS_COLLECTION_NAME);
                DBCursor users = col.find(new BasicDBObject(
                        "films.movieTheaterIdRef", movieTheaterId));

                BasicDBList list = new BasicDBList();
                for (DBObject user : users) {
                    for (Object item : (BasicDBList) user.get("films")) {
                        DBObject current = (DBObject) item;
                        if (current.get("movieTheaterIdRef").equals(
                                movieTheaterId)) {
                            list.add(current.get("filmIdRef"));
                        }
                    }
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(FILMS_COLLECTION_NAME);
                return col.find(new BasicDBObject("_id", new BasicDBObject(
                        "$in", list)));
            }
        });
        List<String> films = new ArrayList<>();
        if (victims != null) {
            for (DBObject current : victims) {
                films.add((String) current.get("title"));
            }
        }
        return films;
    }

    // --------------------------------------------------------------------------------------
    // Obtener los cines en los que un usuario (dado su nombre) ha visto
    // películas:
    // --------------------------------------------------------------------------------------

    public static List<MovieTheater> getWatchedMovieTheatersByUser(
            final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms.getCollection(USERS_COLLECTION_NAME);
                DBObject user = col.findOne(new BasicDBObject("name", name));
                if (user == null)
                    return null;

                BasicDBList list = new BasicDBList();
                for (Object item : (BasicDBList) user.get("films")) {
                    list.add(((DBObject) item).get("movieTheaterIdRef"));
                }
                if (list.size() <= 0)
                    return null;

                col = ms.getCollection(MOVIE_THEATERS_COLLECTION_NAME);
                return col.find(new BasicDBObject("_id", new BasicDBObject(
                        "$in", list)));
            }
        });
        return MongoUtil.toList(MovieTheater.class, victims);
    }

    // --------------------------------------------------------------------------------------
    // Obtener los usuarios que han visto alguna película en un cine concreto:
    // --------------------------------------------------------------------------------------

    public static List<User> getUsersByMovieTheater(final String name) {
        DBCursor victims = MongoUtil.find(DB_NAME, new CustomFind() {
            @Override
            public DBCursor execute(MongoSession ms) {
                DBCollection col = ms
                        .getCollection(MOVIE_THEATERS_COLLECTION_NAME);
                DBObject movieTheater = col.findOne(new BasicDBObject("name",
                        name));
                if (movieTheater == null)
                    return null;

                Object movieTheaterId = movieTheater.get("_id");
                col = ms.getCollection(USERS_COLLECTION_NAME);
                return col.find(new BasicDBObject("films.movieTheaterIdRef",
                        movieTheaterId));
            }
        });
        return MongoUtil.toList(User.class, victims);
    }

}
