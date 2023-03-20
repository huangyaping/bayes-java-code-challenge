package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyingItemsEvent extends DotaEvent {
    protected String item;

    private final static Pattern logPattern = Pattern.compile("\\[(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d)\\] npc_dota_hero_(.+) buys item item_(.+)");

    private BuyingItemsEvent(String time, String heroName, String item) {
        super(time, heroName);
        this.item = item;
    }

    @Override
    protected void supply(CombatLogEntryEntity entity) {
        entity.setItem(item);
        entity.setType(CombatLogEntryEntity.Type.ITEM_PURCHASED);
    }

    public static BuyingItemsEvent fromLog(String log) {
        Matcher matcher = logPattern.matcher(log);
        if (matcher.find()) {
            String time = matcher.group(1);
            String heroName = matcher.group(2);
            String item = matcher.group(3);
            return new BuyingItemsEvent(time, heroName, item);
        }
        return null;
    }

}
