/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.runner.build;

/**
 *
 * @author codex
 */
public interface TileBuilder {
    
    /**
     * Called before each batch of tiles is processed.
     * 
     * @param tools 
     */
    public void preBatch(BuildTools tools); 
    
    /**
     * Processes tile info.
     * 
     * @param tools
     * @return true if a tile should be created from the tile info;
     * false to discard tile info without using it
     */
    public boolean processTile(BuildTools tools);
    
    /**
     * Called after each batch of tiles is processed.
     * 
     * @param tools 
     */
    public void postBatch(BuildTools tools);
    
}
