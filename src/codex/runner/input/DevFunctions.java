/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.input;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

/**
 *
 * @author codex
 */
public class DevFunctions {
    
    public static final String GROUP = "dev-group";
    
    public static final FunctionId
            F_PRINT_STEP = new FunctionId(GROUP, "print-step"),
            F_PRINT_WEIGHT_1 = new FunctionId(GROUP, "print-weight-1"),
            F_PRINT_WEIGHT_2 = new FunctionId(GROUP, "print-weight-2"),
            F_PRINT_WEIGHT_3 = new FunctionId(GROUP, "print-weight-3");
    
    public static void initialize(InputMapper im) {
        im.map(F_PRINT_STEP, KeyInput.KEY_N);
        im.map(F_PRINT_WEIGHT_1, KeyInput.KEY_1);
        im.map(F_PRINT_WEIGHT_2, KeyInput.KEY_2);
        im.map(F_PRINT_WEIGHT_3, KeyInput.KEY_3);
    }
    public static void initialize(GuiGlobals gg) {
        initialize(gg.getInputMapper());
    }
    
}
