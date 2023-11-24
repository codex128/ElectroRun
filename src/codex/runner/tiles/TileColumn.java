/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.tiles;

import codex.boost.ColorHSBA;

/**
 *
 * @author codex
 */
public class TileColumn {
    
    private final int index;
    private int height = 0;
    private float upTendency = 0f;
    private ColorHSBA color = new ColorHSBA();
    
    public TileColumn(int index) {
        this.index = index;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public void setUpTendency(float upTendency) {
        this.upTendency = upTendency;
    }
    public void setColor(ColorHSBA color) {
        this.color = color;
    }
    
    public int getIndex() {
        return index;
    }
    public int getHeight() {
        return height;
    }
    public float getUpTendency() {
        return upTendency;
    }
    public ColorHSBA getColor() {
        return color;
    }
    
}
