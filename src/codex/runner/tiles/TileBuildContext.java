/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.tiles;

import codex.runner.utils.GameConstants;
import com.jme3.math.FastMath;
import jme3utilities.math.noise.Noise2;
import jme3utilities.math.noise.Perlin2;

/**
 *
 * @author codex
 */
public class TileBuildContext {
    
    private Noise2 noise = new Perlin2(20, 5, FastMath.rand.nextLong(), FastMath.rand.nextLong());
    private float spawnPoint = GameConstants.TILE_SIZE.z*7;
    private float killThreshold = -GameConstants.TILE_SIZE.z*3.5f;
    private float columnOffset = 0;
    private float colorChangeRate = 0.03f;
    private int floor = -1;

    public Noise2 getNoise() {
        return noise;
    }
    public void setNoise(Noise2 noise) {
        this.noise = noise;
    }
    public float getSpawnPoint() {
        return spawnPoint;
    }
    public void setSpawnPoint(float spawnPoint) {
        this.spawnPoint = spawnPoint;
    }
    public float getKillThreshold() {
        return killThreshold;
    }
    public void setKillThreshold(float killThreshold) {
        this.killThreshold = killThreshold;
    }
    public float getColumnOffset() {
        return columnOffset;
    }
    public void setColumnOffset(float columnOffset) {
        this.columnOffset = columnOffset;
    }
    public float getColorChangeRate() {
        return colorChangeRate;
    }
    public void setColorChangeRate(float colorChangeRate) {
        this.colorChangeRate = colorChangeRate;
    }
    public int getFloor() {
        return floor;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public int getStepDelay() {
        return (int)(spawnPoint/GameConstants.TILE_SIZE.z)+2;
    }
    
}
