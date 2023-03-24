package gg.bayes.challenge.parser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parser {

    public static List<DotaEvent> parseCombatLog(String combatLog) {
        return combatLog.lines().map(Parser::parseLog)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
    }

    public static Optional<DotaEvent> parseLog(String log) {
        Optional<DotaEvent> optional = Optional.empty();
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
        return optional;
    }

}
