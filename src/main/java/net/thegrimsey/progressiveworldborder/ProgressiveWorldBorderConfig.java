package net.thegrimsey.progressiveworldborder;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "progressiveworldborder")
public class ProgressiveWorldBorderConfig implements ConfigData {
    @Comment("Amount of blocks to expand the border with each minecraft day")
    public double expansionPerDay = 1.0D;
    @Comment("Amount of time it takes to expand the border each day.")
    public double expansionTimeSeconds = 5L;

    @Comment("Base border size. Day amount is added onto this value.")
    public double baseSize = 250.0D;
}
