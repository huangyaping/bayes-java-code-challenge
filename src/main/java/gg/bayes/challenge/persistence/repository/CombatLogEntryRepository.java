package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.rest.model.HeroKills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {

    @Query(value = "select new gg.bayes.challenge.rest.model.HeroKills(actor, count(id)) from CombatLogEntryEntity where type=:type and match.id=:matchId group by actor")
    List<HeroKills> findActorKillsByMatchId(Long matchId, CombatLogEntryEntity.Type type);

    @Query(value = "select item, entry_timestamp as time from dota_combat_log where actor=:actor and match_id=:matchId and entry_type=:type", nativeQuery = true)
    List<Tuple> findHeroItems(Long matchId, String actor, String type);

    @Query(value = "select ability, count(*) as casts from dota_combat_log where actor=:actor and match_id=:matchId and entry_type=:type group by ability", nativeQuery = true)
    List<Tuple> findHeroSpells(Long matchId, String actor, String type);

    @Query(value = "select target, count(*) as damage_instances, sum(damage) as total_damage from dota_combat_log where actor=:actor and match_id=:matchId and entry_type=:type group by target", nativeQuery = true)
    List<Tuple> findHeroDamages(Long matchId, String actor, String type);
}
