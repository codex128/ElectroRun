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
public class LevelChangeBuilder implements TileBuilder {

    private int n = 0;
    
    public LevelChangeBuilder() {}
    public LevelChangeBuilder(int n) {
        this.n = n;
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        tools.tile.setLevelChange(n);
        return true;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
