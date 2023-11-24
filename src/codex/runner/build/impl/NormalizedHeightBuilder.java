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
public class NormalizedHeightBuilder implements TileBuilder {
    
    public enum Mode {
        Absolute, ToHighest, ToLowest, ToAverage;
    }
    
    private final Mode mode;
    private final int height;
    private final boolean calcOnce;
    private Integer h;
    
    public NormalizedHeightBuilder(Mode mode, int height, boolean calcOnce) {
        this.mode = mode;
        this.height = height;
        this.calcOnce = calcOnce;
    }
    
    @Override
    public void preBatch(BuildTools tools) {
        if (calcOnce && h != null) return;
        h = switch (mode) {
            case Absolute -> height;
            case ToHighest -> tools.highest+height;
            case ToLowest -> tools.lowest+height;
            case ToAverage -> tools.average+height;
        };
    }
    @Override
    public boolean processTile(BuildTools tools) {
        tools.column.setHeight(h);
        return true;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
}
