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

    @Query("select eh from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri")
    List<ViewStats> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query("select eh.app, eh.uri, count(eh.ip) " +
            "from EndpointHit eh " +
            "where eh.uri = ?1 and eh.timestamp between ?2 and ?3 " +
            "group by eh.app, eh.uri")
    ViewStats getStatisticsByUri(String uri, LocalDateTime start, LocalDateTime end);


    @Query("select distinct eh.app, eh.uri, count(eh.ip) " +
            "from EndpointHit eh " +
            "where eh.uri = ?1 and eh.timestamp between ?2 and ?3 " +
            "group by eh.app, eh.uri")
    ViewStats getStatisticsByUriUniqueIp(String uri, LocalDateTime start, LocalDateTime end);

    @Query("select distinct eh from EndpointHit eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "group by eh.app, eh.uri")
    List<ViewStats> getAllStatisticsUnique(LocalDateTime start, LocalDateTime end);
}
