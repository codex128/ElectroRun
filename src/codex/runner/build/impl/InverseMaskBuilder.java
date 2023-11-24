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
public class InverseMaskBuilder implements TileBuilder {

    private final TileBuilder delegate, mask;

    public InverseMaskBuilder(TileBuilder delegate, TileBuilder mask) {
        this.delegate = delegate;
        this.mask = mask;
    }
    
    @Override
    public void preBatch(BuildTools tools) {
        delegate.preBatch(tools);
        mask.preBatch(tools);
    }
    @Override
    public boolean processTile(BuildTools tools) {
        return delegate.processTile(tools) != mask.processTile(tools);
    }
    @Override
    public void postBatch(BuildTools tools) {
        delegate.postBatch(tools);
        mask.postBatch(tools);
    }
    
}
