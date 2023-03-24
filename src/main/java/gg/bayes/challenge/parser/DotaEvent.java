package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public abstract class DotaEvent {

    protected String time;
    protected String hero;

    public DotaEvent(String time, String hero) {
        this.time = time;
        this.hero = hero;
    }

    /**
     * Supply customized field value to CombatLogEntryEntity by subclass.
     * @param entity
     */
    protected void supply(CombatLogEntryEntity entity) {
    }

    public CombatLogEntryEntity toEntity() {
        CombatLogEntryEntity entity = new CombatLogEntryEntity();
        long timestamp = TimeUnit.NANOSECONDS.toMillis(LocalTime.parse(time).toNanoOfDay());
        entity.setTimestamp(timestamp);
        entity.setActor(hero);
        supply(entity);
        return entity;
    }

}
