/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.input;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 *
 * @author codex
 */
public class RunnerFunctions {
    
    public static final String GROUP = "runner-group";
    
    public static final FunctionId
            F_STRAFE = new FunctionId(GROUP, "strafe"),
            F_JUMP = new FunctionId(GROUP, "jump");
    
    public static void initialize(GuiGlobals gg) {
        initialize(gg.getInputMapper());
    }
    public static void initialize(InputMapper im) {
        im.map(F_STRAFE, InputState.Positive, KeyInput.KEY_RIGHT);
        im.map(F_STRAFE, InputState.Positive, KeyInput.KEY_D);
        im.map(F_STRAFE, InputState.Negative, KeyInput.KEY_LEFT);
        im.map(F_STRAFE, InputState.Negative, KeyInput.KEY_A);
        im.map(F_JUMP, KeyInput.KEY_UP);
        im.map(F_JUMP, KeyInput.KEY_SPACE);
        im.map(F_JUMP, KeyInput.KEY_W);
    }
    
}
