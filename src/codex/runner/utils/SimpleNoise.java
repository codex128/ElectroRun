/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import codex.boost.math.FDomain;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class SimpleNoise {

    float value = 0f;
    FDomain stepdomain = new FDomain(0f, 1f);
    FDomain globaldomain = new FDomain(null, null);

    public SimpleNoise() {}
    public SimpleNoise(float value) {
        this.value = value;
    }

    public float getNext() {
        boolean pos;
        if (value <= globaldomain.getMin()) {
            pos = true;
        }
        else if (value >= globaldomain.getMax()) {
            pos = false;
        }
        else {
            pos = FastMath.rand.nextBoolean();
        }
        value = globaldomain.applyConstrain(value + Boolean.compare(pos, !pos) * (stepdomain.getMin() + stepdomain.getRangeDistance() * FastMath.rand.nextFloat()));
        return value;
    }
    public float getCurrent() {
        return value;
    }
    
    public FDomain getStepDomain() {
        return stepdomain;
    }
    public FDomain getGlobalDomain() {
        return globaldomain;
    }

}
