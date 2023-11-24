/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import codex.boost.scene.SceneGraphIterator;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;
import jme3utilities.math.noise.Noise2;

/**
 *
 * @author codex
 */
public class GameUtils {
    
    /**
     * Generates a random boolean if a random float (0 to 1) falls
     * within the percentage (inclusive).
     * 
     * @param percent
     * @return percent value &gt;= random value.
     */
    public static boolean random(float percent) {
        return FastMath.nextRandomFloat() <= percent;
    }
    
    /**
     * Sample the noise at the position.
     * 
     * @param noise
     * @param x
     * @param y
     * @return 
     */
    public static float sampleNoiseByInt(Noise2 noise, int x, int y) {
        return noise.sample(GameConstants.NOISE_TO_TILE.x*x, GameConstants.NOISE_TO_TILE.y*y);
    }
    
    /**
     * Returns the length (hypoteneus) of the right-angle triangle.
     * 
     * @param x adjacent side length
     * @param y opposite side length
     * @return length of the hypoteneus side
     */
    public static float length(float x, float y) {
        return FastMath.sqrt(x*x + y*y);
    }
    
    /**
     * Calculates the percent progress of the value between points A and B.
     * 
     * @param value
     * @param a
     * @param b
     * @return percent progress
     */
    public static float progress(float value, float a, float b) {
        return FastMath.clamp((value-a)/(b-a), 0, 1);
    }
    
    /**
     * Replaces the axis value of the vector with the given component value.
     * 
     * @param vec
     * @param component
     * @param axis
     * @return 
     */
    public static Vector3f insertComponent(Vector3f vec, float component, Axis axis) {
        return switch (axis) {
            case X -> new Vector3f(component, vec.y, vec.z);
            case Y -> new Vector3f(vec.x, component, vec.z);
            case Z -> new Vector3f(vec.x, vec.y, component);
        };
    }
    
    /**
     * Fetches the first child named.
     * 
     * @param scene
     * @param name
     * @return 
     */
    public static Spatial getChildNamed(Spatial scene, String name) {
        for (var s : new SceneGraphIterator(scene)) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Randomly returns either -1 or 1.
     * 
     * @return 
     */
    public static int nextRandomSign() {
        return FastMath.rand.nextBoolean() ? 1 : -1;
    }
    
    /**
     * Interpolate between each corresponding component of each vector
     * according the the scalars.
     * 
     * @param a
     * @param b
     * @param sx x component scalar
     * @param sy y component scalar
     * @param sz z component scalar
     * @return linearly interpolated vector
     */
    public static Vector3f lerp(Vector3f a, Vector3f b, float sx, float sy, float sz) {
        return new Vector3f(
            FastMath.interpolateLinear(sx, a.x, b.x),
            FastMath.interpolateLinear(sy, a.y, b.y),
            FastMath.interpolateLinear(sz, a.z, b.z)
        );
    }
    
    /**
     * Perform linear motion and lerp motion, whichever is slower
     * 
     * @param a point a
     * @param b point b
     * @param d linear speed
     * @param s lerp blend
     * @return 
     */
    public static Vector3f linearEase(Vector3f a, Vector3f b, float d, float s) {
        float dist = a.distance(b);
        d = Math.min(d, dist);
        if (d <= dist*s) {
            return b.subtract(a).normalizeLocal().multLocal(d).addLocal(a);
        } else {
            return FastMath.interpolateLinear(s, a, b);
        }
    }
    
    /**
     * Limits a withing a certain distance from b
     * 
     * @param a point a
     * @param b point b
     * @param d maximum distance allowed
     * @return 
     */
    public static Vector3f limitWithinRange(Vector3f a, Vector3f b, float d) {
        if (a.distanceSquared(b) > d*d) {
            return a.subtract(b).normalizeLocal().multLocal(d).addLocal(b);
        }
        else return a;
    }
    
}
