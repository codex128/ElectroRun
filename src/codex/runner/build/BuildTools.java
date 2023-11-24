/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build;

import codex.runner.tiles.TileBatch;
import codex.runner.tiles.TileBuildContext;
import codex.runner.tiles.TileColumn;
import codex.runner.tiles.TileInfo;
import codex.runner.tiles.TileManager;

/**
 *
 * @author codex
 */
public class BuildTools {
    
    public final TileManager manager;
    public final TileBuildContext context;
    public final TileColumn[] columns;
    public TileBatch lastBatch;
    public TileColumn column;
    public TileInfo tile;
    public int globalStep, stackStep, index, height;
    public int highest, lowest, average;
    public float slopeBias = 0;
    
    public BuildTools(TileManager manager) {
        this.manager = manager;
        context = this.manager.getContext();
        columns = this.manager.getColumns();
    }
    
    public void updateGlobals() {
        highest = lowest = context.getFloor()-1;
        average = 0;
        for (var c : manager.getColumns()) {
            if (highest < context.getFloor() || c.getHeight() > highest) {
                highest = c.getHeight();
            }
            if (lowest < context.getFloor() || c.getHeight() < lowest) {
                lowest = c.getHeight();
            }
            average += c.getHeight();
        }
        average /= manager.getColumns().length;
        lastBatch = manager.getBatches().isEmpty() ? null : manager.getBatches().getLast();
        globalStep = manager.getBuildManager().getGlobalStep();
        stackStep = manager.getBuildManager().getStackStep();
    } 
    public void updateSlopeBias(SlopeWeightData data) {
        int step = globalStep+manager.getContext().getStepDelay();
        if (data != null && !data.isEmpty()) {
            if (step == data.getFirst().getStep()) {
                slopeBias = mapSlopeWeight(data.getFirst().getWeight());
                trimWeightData(step, data);
                return;
            }
            trimWeightData(step, data);
        }
        slopeBias = 0;
    }
    public void update(TileColumn column, TileInfo tile) {
        this.column = column;
        this.tile = tile;
        index = this.column.getIndex();
        height = this.column.getHeight();
    }
    
    private float mapSlopeWeight(int n) {
        return switch (n) {
            case 1 -> 0.1f;
            case 2 -> 0.5f;
            case 3 -> 0.9f;
            default -> 0.0f;
        };
    }
    private void trimWeightData(int step, SlopeWeightData data) {
        while (!data.isEmpty()) {
            if (step <= data.getFirst().getStep()) {
                break;
            }
            data.removeFirst();
        }
    }
    
}
