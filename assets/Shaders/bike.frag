
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform sampler2D m_Texture;
uniform float m_Alpha;

#ifdef NB_LIGHTS
    uniform vec4 m_LightColor[NB_LIGHTS];
    uniform vec2 m_LightPosition[NB_LIGHTS];
    uniform float m_Falloff;
    uniform float m_Intensity;
    float extrapolate(float v, float a, float b) {
        return clamp((v-a)/(b-a), 0.0, 1.0);
    }
    float interpolate(float v, float a, float b) {
        return clamp(v, 0.0, 1.0)*(b-a)+a;
    }
#endif

varying vec3 wPosition;
varying vec3 wNormal;
varying vec2 texCoord;

void main() {
    
    gl_FragColor = texture2D(m_Texture, texCoord);
    gl_FragColor.a *= m_Alpha;
    
    #ifdef NB_LIGHTS
        vec3 lightColor = vec4(1.0);
        vec2 localPos = wPosition.xy;
        for (int i = 0; i < NB_LIGHTS; i++) {
            float e = distance(localPos, m_LightPosition[i]);
            vec2 n = normalize(m_LightPosition[i] - localPos);
            float factor = interpolate(extrapolate(e, m_Falloff, 0.0), 1.0, 0.0) * dot(wNormal, n);
            lightColor = mix(lightColor, m_LightColor[i].rgb, factor);
        }
        gl_FragColor.rgb = gl_FragColor.rgb * lightColor;
    #endif
    
}
