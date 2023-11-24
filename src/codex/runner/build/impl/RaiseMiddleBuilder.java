/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import codex.runner.tiles.TileColumn;

/**
 *
 * @author codex
 */
public class RaiseMiddleBuilder implements TileBuilder {

    private final int height;

    public RaiseMiddleBuilder(int height) {
        this.height = height;
    }
    
    @Override
    public void preBatch(BuildTools tools) {}
    @Override
    public boolean processTile(BuildTools tools) {
        return true;
    }
    @Override
    public void postBatch(BuildTools tools) {
        int i = tools.columns.length/2;
        tools.columns[i].setHeight(getNeighboringHeight(i, tools.columns)+height);
    }
    
    private int getNeighboringHeight(int i, TileColumn[] columns) {
        if (i == 0) return columns[i+1].getHeight();
        else if (i == columns.length-1) return columns[i-1].getHeight();
        else return Math.max(columns[i+1].getHeight(), columns[i-1].getHeight());
    }
    
}
