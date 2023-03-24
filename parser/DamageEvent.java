package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DamageEvent extends DotaEvent {
    protected String targetHero;
    protected Integer damage;

    private final static Pattern logPattern = Pattern.compile("\\[(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d)\\] npc_dota_hero_(.+) hits npc_dota_hero_(.+) with (.+) for (\\d+) damage");

    private DamageEvent(String time, String heroName, String target, Integer damage) {
        super(time, heroName);
        this.targetHero = target;
        this.damage = damage;
    }

    @Override
    protected void supply(CombatLogEntryEntity entity) {
        entity.setDamage(damage);
        entity.setTarget(targetHero);
        entity.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);
    }

    public static Optional<DotaEvent> fromLog(String log) {
        Matcher matcher = logPattern.matcher(log);
        if (matcher.find()) {
            String time = matcher.group(1);
            String hero = matcher.group(2);
            String target = matcher.group(3);
            Integer damage = Integer.valueOf(matcher.group(5));
            return Optional.of(new DamageEvent(time, hero, target, damage));
        }

        return Optional.empty();
    }

}
