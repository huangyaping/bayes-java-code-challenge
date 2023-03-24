package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CastingSpellsEvent extends DotaEvent {
    protected String spellName;
    protected Integer level;

    private CastingSpellsEvent(String time, String heroName, String spellName, Integer level) {
        super(time, heroName);
        this.spellName = spellName;
        this.level = level;
    }

    private final static Pattern logPattern = Pattern.compile("\\[(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d)\\] npc_dota_hero_(.+) casts ability (.+) \\(lvl (\\d+)\\)");

    @Override
    protected void supply(CombatLogEntryEntity entity) {
        entity.setType(CombatLogEntryEntity.Type.SPELL_CAST);
        entity.setAbility(spellName);
        entity.setAbilityLevel(level);
    }

    public static Optional<DotaEvent> fromLog(String log) {
        Matcher matcher = logPattern.matcher(log);
        if (matcher.find()) {
            String time = matcher.group(1);
            String heroName = matcher.group(2);
            String spellName = matcher.group(3);
            Integer level = Integer.valueOf(matcher.group(4));
            return Optional.of(new CastingSpellsEvent(time, heroName, spellName, level));
        }
        return Optional.empty();
    }

}
