/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class Random {
    
    private float chance;
    private float random = -1f;
    
    public Random() {
        this(0.5f);
    }
    public Random(float chance) {
        this.chance = chance;
    }
    
    public boolean generate() {
        random = FastMath.nextRandomFloat();
        return pass();
    }
    public boolean generate(float bias) {
        random = FastMath.nextRandomFloat();
        return pass(bias);
    }
    public boolean generate(float bias, boolean useBias) {
        random = FastMath.nextRandomFloat();
        return pass(bias, useBias);
    }
    public boolean pass() {
        return random <= chance;
    }
    public boolean pass(float bias) {
        return random <= chance+bias;
    }
    public boolean pass(float bias, boolean useBias) {
        return random <= chance+(useBias ? bias : 0);
    }
    
    public void setChance(float chance) {
        this.chance = chance;
    }
    
    public float getChance() {
        return chance;
    }
    public float getRandom() {
        return random;
    }
    
}
