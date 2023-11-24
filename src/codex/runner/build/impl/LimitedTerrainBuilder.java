/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import codex.runner.utils.IntRange;
import codex.runner.utils.Random;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class LimitedTerrainBuilder implements TileBuilder {

    private Random buildRandom = new Random();
    private Random slopeRandom = new Random();
    private IntRange range = new IntRange(-1, 1);
    
    public LimitedTerrainBuilder(float buildChance, float slopeChance, int min, int max) {
        buildRandom.setChance(buildChance);
        slopeRandom.setChance(slopeChance);
        range.setMin(min);
        range.setMax(max);
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        if (!buildRandom.generate(-tools.slopeBias)) {
            return false;
        }
        if (slopeRandom.generate(tools.slopeBias)) {
            if (tools.column.getHeight() <= range.getMin()) {
                tools.tile.setLevelChange(1);
            } else if (tools.column.getHeight() >= range.getMax()) {
                tools.tile.setLevelChange(-1);
            } else {
                float f = FastMath.nextRandomFloat();
                if (f < .5f+tools.slopeBias) {
                    tools.tile.setLevelChange(1);
                } else {
                    tools.tile.setLevelChange(-1);
                }
            }
        }
        return true;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
