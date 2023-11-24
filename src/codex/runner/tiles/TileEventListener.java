/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.runner.tiles;

import codex.runner.build.BuildStack;

/**
 *
 * @author codex
 */
public interface TileEventListener {
    
    /**
     * Called when a tile batch is created.
     * 
     * @param batch 
     */
    public void onBatchCreated(TileBatch batch);
    
    /**
     * Called when a tile batch is destroyed.
     * 
     * @param batch 
     */
    public void onBatchDestroyed(TileBatch batch);
    
    /**
     * Called when the manager switches to another build stack.
     * <p>
     * Warning: this method may be called before or after the actual switch
     * is made. Do not rely on {@link TileManager#getCurrentStack()} to agree
     * with the arguments passed.
     * 
     * @param prev stack being switched from (or null, if no stack was previously in use)
     * @param next next stack being switched to (or null, if none available)
     */
    public void onBuildStackSwitched(BuildStack prev, BuildStack next);
    
}
