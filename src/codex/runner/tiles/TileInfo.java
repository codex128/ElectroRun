/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.tiles;

import codex.boost.ColorHSBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class TileInfo {
    
    public static final int NORMAL = 0, VAPORIZER = 1;
    public static final TileInfo IDENTITY = new TileInfo(0, 0);
    
    private final int column, height;
    private int type = NORMAL;
    private Vector3f position = Vector3f.ZERO;
    private ColorHSBA color = new ColorHSBA();
    private int levelChange = 0;
    private float noiseSample = 0;
    
    public TileInfo(int column, int height) {
        this.column = column;
        this.height = height;
    }
    public TileInfo(TileInfo info) {
        this(info.column, info.height);
        set(info);
    }
    
    public final void set(TileInfo info) {
        type = info.type;
        position = new Vector3f(info.position);
        color = new ColorHSBA(info.color);
        levelChange = info.levelChange;
        noiseSample = info.noiseSample;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void setColor(ColorHSBA color) {
        this.color = color;
    }
    public void setLevelChange(int levelChange) {
        this.levelChange = levelChange;
    }
    public void setNoiseSample(float sample) {
        this.noiseSample = sample;
    }
    
    public int getColumn() {
        return column;
    }
    public int getHeight() {
        return height;
    }
    public int getType() {
        return type;
    }
    public Vector3f getPosition() {
        return position;
    }
    public ColorHSBA getColor() {
        return color;
    }
    public int getLevelChange() {
        return levelChange;
    }
    public float getNoiseSample() {
        return noiseSample;
    }
    
}
