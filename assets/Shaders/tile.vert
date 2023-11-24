
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec4 inColor;

varying vec3 wPosition;
varying vec2 texCoord;
varying vec4 wColor;

void main() {
    
    vec4 modelSpacePos = vec4(inPosition, 1.0);
    
    wPosition = TransformWorld(modelSpacePos).xyz;
    texCoord = inTexCoord;
    wColor = inColor;
    
    gl_Position = TransformWorldViewProjection(modelSpacePos);
    
}
