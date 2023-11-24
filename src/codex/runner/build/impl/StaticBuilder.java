/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;

/**
 * Builds tiles according to what tiles were built last batch.
 * 
 * @author codex
 */
public class StaticBuilder implements TileBuilder {
    
    private int grow = 0;
    private int rate = 1;
    private int time = 0, g = 0;
    private int delay = 0;
    private boolean keepMiddle = true;
    private boolean blank;
    
    public StaticBuilder() {}
    public StaticBuilder(int grow, int rate, int delay) {
        this.grow = grow;
        this.rate = rate;
        this.delay = delay;
    }
    
    @Override
    public void preBatch(BuildTools tools) {
        if (delay-- <= 0 && ++time >= rate) {
            time = 0;
            g = Math.abs(grow);
        }
        else {
            g = 0;
        }
        if (tools.lastBatch != null) {
            blank = true;
            for (var t : tools.lastBatch.getTileArray()) if (t != null) {
                blank = false;
                break;
            }
        }
    }
    @Override
    public boolean processTile(BuildTools tools) {
        if (tools.lastBatch == null || blank) {
            return tools.column.getIndex() == tools.manager.getColumns().length/2;
        }
        if (keepMiddle && tools.column.getIndex() == tools.manager.getColumns().length/2) {
            return true;
        }
        for (int i = Math.max(tools.index-g, 0); i <= Math.min(tools.index+g, tools.columns.length-1); i++) {
            if (grow >= 0 && tools.lastBatch.getTileArray()[i] != null) {
                return true;
            }
            else if (grow < 0 && (tools.lastBatch.getTileArray()[i] == null || i == 0 || i == tools.columns.length-1)) {
                return false;
            }
        }
        return grow < 0;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
