package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and eh.uri in ?3 " +
            "group by eh.app, eh.uri")
    List<ViewStats> getStatisticsByUri(LocalDateTime start, LocalDateTime end, String[] uris);


    @Query("select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and eh.uri in ?3 " +
            "group by eh.app, eh.uri")
    List<ViewStats> getStatisticsByUriUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select eh, count(distinct eh.ip) " +
            "from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getAllStatisticsUnique(LocalDateTime start, LocalDateTime end);
}
