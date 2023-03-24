package gg.bayes.challenge.rest.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class HeroKills {
    String hero;
    Long kills;
}
