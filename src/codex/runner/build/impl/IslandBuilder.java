/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;

/**
 *
 * @author codex
 */
public class IslandBuilder implements TileBuilder {
    
    private final float threshold;
    
    public IslandBuilder(float threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        //System.out.println("noise-sample="+tools.tile.getNoiseSample());
        return tools.tile.getNoiseSample() <= threshold;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
