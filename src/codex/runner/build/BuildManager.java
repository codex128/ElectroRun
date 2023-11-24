/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build;

import codex.runner.tiles.TileBuildContext;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class BuildManager {
    
    private LinkedList<BuildStack> stacks = new LinkedList<>();
    private BuildStack defaultStack;
    private int stackStep = 0;
    private int globalStep = 0;
    
    public BuildManager(BuildStack defaultStack) {
        this.defaultStack = defaultStack;
    }
    
    public void updateStep(int delay) {
        globalStep++;
        stackStep++;
        if (!isUsingDefault() && (getCurrentStack().getDuration().readyToEnd(globalStep+delay)
                || (hasNextStack() && stacks.get(1).getDuration().readyToStart(globalStep+delay)))) {
            removeCurrentStack();
        }
    }
    public void reset() {
        stacks.clear();
        globalStep = 0;
    }
    
    public void addBuildStack(BuildStack stack) {
        assert stack != null;
        if (stacks.isEmpty()) {
            stackStep = 0;
        }
        stacks.addLast(stack);
    }
    public void addBuildStacks(Collection<BuildStack> stacks) {
        if (stacks.isEmpty()) return;
        if (this.stacks.isEmpty()) {
            stackStep = 0;
        }
        this.stacks.addAll(stacks);
    }
    public void forceBuildStack(BuildStack stack) {
        assert stack != null;
        stacks.addFirst(stack);
        stackStep = 0;
    }
    public void removeCurrentStack() {
        if (stacks.isEmpty()) return;
        if (!stacks.isEmpty()) {
            stackStep = 0;
        }
        stacks.removeFirst();
    }
    public void clearStackList() {
        if (!stacks.isEmpty()) {
            stacks.clear();
            stackStep = 0;
        }
    }
    
    public BuildStack getCurrentStack() {
        return !stacks.isEmpty() ? stacks.getFirst() : defaultStack;
    }
    public BuildStack getNextStack() {
        return hasNextStack() ? stacks.get(1) : null;
    }
    public boolean isUsingDefault() {
        return stacks.isEmpty();
    }
    public boolean hasNextStack() {
        return stacks.size() >= 2;
    }
    
    public int getGlobalStep() {
        return globalStep;
    }
    public int getStackStep() {
        return stackStep;
    }
    
}
