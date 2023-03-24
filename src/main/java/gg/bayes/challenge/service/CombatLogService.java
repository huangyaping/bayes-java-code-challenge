package gg.bayes.challenge.service;

import gg.bayes.challenge.parser.Parser;
import gg.bayes.challenge.parser.DotaEvent;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CombatLogService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CombatLogEntryRepository combatLogEntryRepository;

    @Transactional
    public Long ingestCombatLog(String combatLog) {
        List<DotaEvent> events = Parser.parseCombatLog(combatLog);
        MatchEntity match = new MatchEntity();
        for (DotaEvent event : events) {
            match.addCombatLog(event.toEntity());
        }
        matchRepository.save(match);
        return match.getId();
    }

    public List<HeroKills> getMatch(Long matchId) {
        return combatLogEntryRepository.findActorKillsByMatchId(matchId, CombatLogEntryEntity.Type.HERO_KILLED);
    }

    private <T> List<T> mapList(List<Tuple> list, Function<Tuple, T> mapFunction) {
        return list.stream()
                .map(mapFunction)
                .collect(Collectors.toList());
    }

    public List<HeroItem> getHeroItems(Long matchId, String heroName) {
        List<Tuple> list = combatLogEntryRepository.findHeroItems(matchId,
                heroName, CombatLogEntryEntity.Type.ITEM_PURCHASED.toString());

        return mapList(list, tuple -> new HeroItem(tuple.get("item", String.class),
                        tuple.get("time", BigInteger.class).longValue())
        );
    }

    public List<HeroSpells> getHeroSpells(Long matchId, String heroName) {
        List<Tuple> list = combatLogEntryRepository.findHeroSpells(matchId,
                heroName, CombatLogEntryEntity.Type.SPELL_CAST.toString());

        return mapList(list, tuple -> new HeroSpells(tuple.get("ability", String.class),
                tuple.get("casts", BigInteger.class).intValue())
        );
    }

    public List<HeroDamage> getHeroDamages(Long matchId, String heroName) {
        List<Tuple> list = combatLogEntryRepository.findHeroDamages(matchId,
                heroName, CombatLogEntryEntity.Type.DAMAGE_DONE.toString());

        return mapList(list, tuple -> new HeroDamage(tuple.get("target", String.class),
                tuple.get("damage_instances", BigInteger.class).intValue(),
                tuple.get("total_damage", BigInteger.class).intValue())
        );
    }
}
