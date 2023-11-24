/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import codex.runner.tiles.TileColumn;
import codex.runner.utils.Random;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class AdvancedTerrainBuilder implements TileBuilder {
    
    private final int globalThreadMin = -1;
    private final int maxThreadGap = 20; //10
    private float real = 1f;
    private float discardBoost = 0f;
    
    public AdvancedTerrainBuilder(float real, float discard) {
        this.real = real;
        this.discardBoost = discard;
    }

    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        //if (tools.global.globalStep == 0) return false;
        float discard = FastMath.rand.nextFloat()+tools.slopeBias*0.5f;
        if ((tools.column.getHeight() == tools.lowest && discard < .25f+discardBoost)
                || discard < .4f+discardBoost) {
            return false;
        }
        //if (tools.tile.getNoiseSample() < .02f) {
        //    return false;
        //}
        boolean heightlimit = tools.highest-tools.lowest >= maxThreadGap;
        float flatchance = FastMath.rand.nextFloat();
        TileColumn n1 = null, n2 = null;
        if (tools.index > 0) {
            n1 = tools.columns[tools.index - 1];
        }
        if (tools.index < tools.columns.length - 1) {
            n2 = tools.columns[tools.index + 1];
        }
        boolean hasGreaterNeigbor = (n1 != null && n1.getHeight() > tools.column.getHeight())
                || (n2 != null && n2.getHeight() > tools.column.getHeight());
        if (flatchance < .8f-tools.slopeBias) {
            float realchance = FastMath.rand.nextFloat();
            if (realchance < real) {
                //global.ed.setComponents(id,
                //        new Visual(GameModelFactory.PANEL, "game_collision"),
                //        new Color(tools.thread.getColorRGBA
                //()));
            }
            else {
                //global.ed.setComponents(id,
                //        new Visual(GameModelFactory.BROKEN_PANEL),
                //        new Color(ColorRGBA.Gray),
                //        new Active(false));
                return false;
            }
        }
        else {
            float upchance = FastMath.rand.nextFloat();
            boolean up = upchance < .5f +tools.slopeBias +tools.column.getUpTendency()*.02f
                    || (hasGreaterNeigbor && upchance < .8f);
            if ((heightlimit && tools.column.getHeight() == tools.highest)
                    || (!up && (!heightlimit || tools.column.getHeight() > tools.lowest))
                    && tools.column.getHeight() != globalThreadMin) {
                tools.tile.setLevelChange(-1);
            }
            else {
                tools.tile.setLevelChange(1);
            }
        }
        return true;
    }
    @Override
    public void postBatch(BuildTools tools) {}

}
