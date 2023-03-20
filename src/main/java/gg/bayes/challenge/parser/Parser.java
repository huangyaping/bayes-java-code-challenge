package gg.bayes.challenge.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parser {

    public static List<DotaEvent> parseCombatLog(String combatLog) {
        return combatLog.lines().map(Parser::parseLog)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static DotaEvent parseLog(String log) {
        if (log.contains("buys")) {
            return BuyingItemsEvent.fromLog(log);
        }
        if (log.contains("is killed by")) {
            return KillingHeroesEvent.fromLog(log);
        }
        if (log.contains("casts")) {
            return CastingSpellsEvent.fromLog(log);
        }
        if (log.contains("hits")) {
            return DamageEvent.fromLog(log);
        }
        return null;
    }

}
