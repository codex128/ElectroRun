/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build;

import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class SlopeWeightData extends LinkedList<SlopeWeight> {
    
    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public SlopeWeightData clone() {
        var clone = new SlopeWeightData();
        clone.addAll(this);
        return clone;
    }
    
}
