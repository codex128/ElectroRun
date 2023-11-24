/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner;

import codex.boost.scene.SceneGraphIterator;
import codex.runner.anim.AlertArmatureMask;
import codex.runner.tiles.TileManager;
import codex.runner.tiles.TileBatch;
import codex.runner.anim.AnimLayerControl;
import codex.runner.anim.MaskedBlendAction;
import codex.runner.mesh.PointAttributes;
import codex.runner.mesh.PointInfluencer;
import codex.runner.utils.Elastic;
import codex.runner.utils.ElasticAction;
import codex.runner.utils.GameConstants;
import codex.runner.utils.GameUtils;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.LinearBlendSpace;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;

/**
 *
 * @author codex
 */
public class Runner implements PointInfluencer {
    
    private Spatial spatial;
    private Material material;
    private AnimComposer anim;
    private SkinningControl skin;
    private AnimLayerControl layers;
    private Vector3f position = new Vector3f(Vector3f.ZERO);
    private final Vector3f velocity = new Vector3f(0f, 0f, GameConstants.TILE_SIZE.z*GameConstants.BPS*2); //z=35
    private final Vector2f accel = new Vector2f(45f, 10f); // +-x, +y
    private final Vector3f smoothVelocity = new Vector3f(velocity);
    private final float terminal = 9f;
    private final float decel = 55f; //65
    private final float gravity = 35f;
    private final float slopeAccel = 25f;
    private final Elastic tiltAngle = new Elastic();
    private final Elastic steerValue = new Elastic();
    private boolean jump = true;
    private final Wheel[] wheels = new Wheel[2];
    private final float wheelRadius = .4f;
    private final Vector2f input = new Vector2f();
    private int step = 0;
    
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        for (var s : new SceneGraphIterator(spatial)) {
            if (s instanceof Geometry) {
                material = ((Geometry)s).getMaterial();
                break;
            }
        }
        if (material == null) {
            throw new NullPointerException("Unable to locate material.");
        }
        initializeAnimations();
        initializeWheels();
    }
    private void initializeAnimations() {
        
        anim = spatial.getControl(AnimComposer.class);
        skin = spatial.getControl(SkinningControl.class);
        
        layers = new AnimLayerControl();
        spatial.addControl(layers);
        layers.create(new AlertArmatureMask("main", anim, skin).addFromJoint("lean"));
        layers.create(new AlertArmatureMask("lean", anim, skin).addJoints("lean"));
        layers.create(new AlertArmatureMask("steer", anim, skin).addJoints("steering"));
        //layers.create(new AlertArmatureMask("wheelie", anim, skin).addJoints("rear-tilt"));
        //layers.create(new AlertArmatureMask("airborne", anim, skin).addFromJoint("lean"));
        //layers.create(new AlertArmatureMask("stunt", anim, skin).addFromJoint("lean"));
        
        //layers.create("lean", ArmatureMask.createMask(skin.getArmature(), "lean"));
        
        var steer = MaskedBlendAction.create(anim, "steer-blend", new LinearBlendSpace(0, 1), "steer-left", "steer-right");
        steer.getBlendSpace().setValue(0.5f);
        steer.setMaxTransitionWeight(.9f);
        
        var lean = MaskedBlendAction.create(anim, "lean-blend", new LinearBlendSpace(0, 1), "lean-left", "lean-right");
        lean.getBlendSpace().setValue(0.5f);
        lean.setMaxTransitionWeight(.9f);
        
        layers.enter("main", "normal");
        layers.enter("steer", "steer-blend");
        layers.enter("lean", "lean-blend");
        //layers.enter("steer", "steer-left");
        //layers.enter("lean", "lean-left");
        
        tiltAngle.setValue(0);
        tiltAngle.addAction("ground", new ElasticAction(.5f, Elastic.Mode.Lerp));
        tiltAngle.addAction("air", new ElasticAction(.9f, Elastic.Mode.Linear));
        steerValue.setValue(.5f);
        steerValue.addAction("lean-to-accel", new ElasticAction(5f, Elastic.Mode.Linear));
        steerValue.addAction("lean-to-decel", new ElasticAction(10f, Elastic.Mode.Linear));
        steerValue.addAction("return-to-center", new ElasticAction(5f, Elastic.Mode.Linear));
        
    }
    private void initializeWheels() {
        wheels[0] = new Wheel(GameUtils.getChildNamed(spatial, "front-wheel"), wheelRadius);
        wheels[1] = new Wheel(GameUtils.getChildNamed(spatial, "rear-wheel"), wheelRadius);
    }
    private BlendAction getBlendAction(String name) {
        return (BlendAction)anim.action(name);
    }
    
    public void update(TileManager tileManager, float tpf) {        
        position.set(spatial.getWorldTranslation());
        if (DevSettings.USE_GRAVITY) {
            velocity.y -= gravity*tpf;
        } else {
            velocity.y = 0;
        }
        if (jump && input.y > 0) {
            velocity.y = accel.y;
            jump = false;
        }        
        if (input.x != 0) {
            float a = accel.x;
            if (FastMath.sign(velocity.x) != FastMath.sign(input.x)) {
                a += decel;
            }
            velocity.x = FastMath.clamp(velocity.x+a*FastMath.sign(input.x)*tpf, -terminal, terminal);
            steerValue.setCurrentAction("lean-to-accel", -FastMath.sign(input.x));
        }
        else if (FastMath.abs(velocity.x) < decel*tpf) {
            velocity.x = 0;
            steerValue.setCurrentAction("return-to-center", 0);
        }
        else {
            velocity.x -= decel*FastMath.sign(velocity.x)*tpf;
            steerValue.setCurrentAction("lean-to-decel", FastMath.clamp(velocity.x/10, -.5f, .5f));
        }     
        FastMath.interpolateLinear(.1f, smoothVelocity, velocity, smoothVelocity);
        if (!performCollisions(tileManager, tpf)) {
            spatial.move(0f, velocity.y*tpf, 0f);
        }
        spatial.move(velocity.x*tpf, 0f, 0f);
        updateSteeringPose(tpf);
        updateTiltPose(tpf);
        updateCameraPosition(tileManager.getApplication().getCamera(), tpf);
        input.set(0, 0);
    }
    private void updateSteeringPose(float tpf) {
        steerValue.update(tpf);
        float progress = FastMath.clamp((steerValue.getValue()+1)/2, 0.001f, 1f);
        getBlendAction("steer-blend").getBlendSpace().setValue(progress);
        getBlendAction("lean-blend").getBlendSpace().setValue(progress);
    }
    private void updateTiltPose(float tpf) {
        tiltAngle.update(tpf);
        skin.getArmature().getJoint("front-tilt").setLocalRotation(
                new Quaternion().fromAngleAxis(tiltAngle.getValue(), Vector3f.UNIT_X));
    }
    private void updateCameraPosition(Camera cam, float tpf) {
        Vector3f posTarget = spatial.getLocalTranslation().add(0f, 5.05f, -15f);
        Vector3f viewTarget = spatial.getLocalTranslation().add(0f, 1f, 0f).subtractLocal(cam.getLocation()).normalizeLocal();
        viewTarget.setY(GameUtils.linearEase(cam.getDirection(), viewTarget, 1f*tpf, .1f).normalizeLocal().y);
        cam.setLocation(GameUtils.linearEase(cam.getLocation(), posTarget, 22f*tpf, .1f));
        cam.setLocation(GameUtils.limitWithinRange(cam.getLocation(), spatial.getLocalTranslation(), 20));
        cam.lookAtDirection(viewTarget, Vector3f.UNIT_Y);
    }    
    
    @Override
    public void influence(PointAttributes p, float tpf) {
        p.point.subtractLocal(0f, 0f, velocity.z*tpf*0.5f);
    }
    
    private boolean performCollisions(TileManager tileManager, float tpf) {
        
        CollisionResult[] contact = new CollisionResult[wheels.length];
        boolean[] collision = new boolean[wheels.length];
        for (int i = 0; i < contact.length; i++) {
            contact[i] = performLineTest(tileManager, wheels[i].getCenter());
            collision[i] = collisionWithinWheel(contact[i], wheels[i], tpf);
        }
        
        for (int i = 0; i < contact.length; i++) {
            if (collision[i]) {
                jump = true;
                velocity.y = Math.max(-contact[i].getContactNormal().z*slopeAccel, 0);
                break;
            }
        }
        
        if (collision[0] && contact[1] != null && contact[1].getDistance() < GameConstants.TILE_SIZE.y) {
            Vector3f diff = contact[0].getContactPoint().subtract(contact[1].getContactPoint());
            diff.normalizeLocal();
            tiltAngle.setCurrentAction("ground", diff.angleBetween(Vector3f.UNIT_Z));
        }
        else {
            tiltAngle.setCurrentAction("air", 0f);
        }
        
        Float y = null;
        for (int i = 0; i < contact.length; i++) {
            if (collision[i] && (y == null || y < contact[i].getContactPoint().y)) {
                y = contact[i].getContactPoint().y;
            }
        }
        if (y != null) {
            spatial.setLocalTranslation(GameUtils.insertComponent(spatial.getLocalTranslation(), y, Axis.Y));
            return true;
        }
        
        return collision[0] || collision[1];
        
    }
    private CollisionResult performLineTest(TileManager tileManager, Vector3f position) {
        TileBatch batch = tileManager.getBatchByPosition(position);
        if (batch != null) {
            CollisionResults results = new CollisionResults();
            Ray ray1 = new Ray();
            Ray ray2 = new Ray();
            ray2.origin.set(ray1.origin.set(position));
            ray2.direction.set(ray1.direction.set(Vector3f.UNIT_Y)).negateLocal();
            batch.getGeometry().collideWith(ray1, results);
            batch.getGeometry().collideWith(ray2, results);
            if (results.size() > 0) {
                return results.getCollision(0);
            }
        }
        return null;
    }
    private boolean collisionWithinWheel(CollisionResult result, Wheel wheel, float tpf) {
        return result != null && result.getDistance() <= wheel.radius-velocity.y*tpf &&
                (velocity.y < 0 || result.getContactNormal().z < 0);
    }
    
    public void setInputVector(Vector2f input) {
        this.input.set(input);
    }
    public void incrementStep() {
        step++;
    }
    public void setForwardVelocity(float speed) {
        velocity.z = speed;
    }
    
    public Spatial getSpatial() {
        return spatial;
    }
    public Material getMaterial() {
        return material;
    }
    public Vector3f getPosition() {
        return position;
    }
    public Vector2f getInputVector() {
        return input;
    }
    public Vector3f getVelocity() {
        return velocity;
    }
    public int getStepNum() {
        return step;
    }
    
    private static class Wheel {
        
        Spatial spatial;
        float radius;
        
        public Wheel(Spatial spatial, float radius) {
            this.spatial = spatial;
            this.radius = radius;
        }
        
        public Vector3f getPosition() {
            return spatial.getWorldTranslation();
        }
        public Vector3f getCenter() {
            return getPosition().add(0f, radius, 0f);
        }
        
    }
    
}
