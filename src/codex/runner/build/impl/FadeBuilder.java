/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import codex.runner.utils.GameUtils;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class FadeBuilder implements TileBuilder {

    private final int distance;
    private final float bias = -0.12f;

    public FadeBuilder(int distance) {
        this.distance = distance;
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        return FastMath.rand.nextFloat() > GameUtils.progress(tools.stackStep, 0, distance)-bias;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
