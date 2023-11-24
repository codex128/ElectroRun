/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimationMask;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;

/**
 *
 * @author codex
 */
public class MaskedBlendAction extends BlendAction {
    
    public MaskedBlendAction(BlendSpace blendSpace, BlendableAction... actions) {
        super(blendSpace, actions);
    }
    
    @Override
    public void setMask(AnimationMask mask) {
        super.setMask(mask);
        // propagate mask to contained actions
        for (Action action : actions) {
            action.setMask(mask);
        }
    }
    
    public static MaskedBlendAction create(AnimComposer anim, String name, BlendSpace blendSpace, String... clips) {
        // This code is identical to AnimComposer#actionBlended, but changed
        // to use MaskedBlendAction instead.
        BlendableAction[] acts = new BlendableAction[clips.length];
        for (int i = 0; i < acts.length; i++) {
            BlendableAction ba = (BlendableAction)anim.makeAction(clips[i]);
            acts[i] = ba;
        }
        MaskedBlendAction action = new MaskedBlendAction(blendSpace, acts);
        anim.addAction(name, action);
        return action;
    }
    
}
