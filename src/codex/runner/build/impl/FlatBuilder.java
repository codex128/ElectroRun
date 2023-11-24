/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import codex.runner.utils.Random;

/**
 *
 * @author codex
 */
public class FlatBuilder implements TileBuilder {

    private final Random buildRandom = new Random();
    
    public FlatBuilder(float chance) {
        buildRandom.setChance(chance);
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        return buildRandom.generate();
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
