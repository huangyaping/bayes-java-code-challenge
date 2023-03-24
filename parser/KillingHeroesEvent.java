package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KillingHeroesEvent extends DotaEvent {
    protected String diedHero;

    private final static Pattern logPattern = Pattern.compile("\\[(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d)\\] npc_dota_hero_(.+) is killed by npc_dota_hero_(.+)");

    private KillingHeroesEvent(String time, String heroName, String killedHero) {
        super(time, heroName);
        this.diedHero = killedHero;
    }

    @Override
    protected void supply(CombatLogEntryEntity entity) {
        entity.setTarget(diedHero);
        entity.setType(CombatLogEntryEntity.Type.HERO_KILLED);
    }

    public static Optional<DotaEvent> fromLog(String log) {
        Matcher matcher = logPattern.matcher(log);
        if (matcher.find()) {
            String time = matcher.group(1);
            String killed = matcher.group(2);
            String hero = matcher.group(3);
            return Optional.of(new KillingHeroesEvent(time, hero, killed));
        }
        return Optional.empty();
    }

}
