/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build;

/**
 *
 * @author codex
 */
public class SlopeWeight {
    
    private final int step;
    private int weight = 0;

    public SlopeWeight(int step) {
        this.step = step;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStep() {
        return step;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.step;
        hash = 61 * hash + this.weight;
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SlopeWeight other = (SlopeWeight)obj;
        if (this.step != other.step) {
            return false;
        }
        return this.weight == other.weight;
    }
    
}
