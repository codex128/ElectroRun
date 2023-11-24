/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class GameConstants {
    
    /**
     * Ratio between noise scale and tile scale.
     */
    public static final Vector2f NOISE_TO_TILE = new Vector2f(.2f, .4f);
    
    /**
     * Size of each tile.
     * <p>
     * X=width, Y=height, Z=depth
     */
    public static final Vector3f TILE_SIZE = new Vector3f(1, 5, 10);
    
    /**
     * Beats per second.
     */
    public static final float BPS = 2.4f;
    
}
