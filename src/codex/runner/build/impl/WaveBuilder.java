/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.impl;

import codex.runner.build.BuildTools;
import codex.runner.build.TileBuilder;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class WaveBuilder implements TileBuilder {
    
    private int[] values = new int[6];
    private int stroke = 1;
    private float amplitude = 20.5f;
    private float wavelength = 0.1f;
    
    @Override
    public void preBatch(BuildTools tools) {
        float value = wavelength*tools.globalStep;
        int center = tools.columns.length/2;
        values[0] = sin(value, amplitude/2, center);
        values[1] = sin(value, -amplitude/2, center);
        values[2] = sin(value*4, amplitude, center);
        values[3] = sin(value*4, -amplitude, center);
        values[4] = cos(value, amplitude, center);
        values[5] = cos(value, -amplitude, center);
    }
    @Override
    public boolean processTile(BuildTools tools) {
        for (int v : values) {
            if (tools.index >= v-stroke && tools.index <= v+stroke) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void postBatch(BuildTools tools) {}
    
    private int sin(float value, float radius, int center) {
        return (int)(FastMath.sin(value)*radius)+center;
    }
    private int cos(float value, float radius, int center) {
        return (int)(FastMath.cos(value)*radius)+center;
    }
    
}
