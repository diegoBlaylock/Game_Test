#version 460

layout (location =0) in vec3 pos;
layout (location =1) in vec2 uv;

layout(location=1) out vec2 uvOut;

uniform vec2 offset;
uniform float scale;
uniform mat4 proj;

void main()
{
	vec3 scaled = scale * pos;
    gl_Position = proj * vec4(scaled.xy + offset, scaled.z,  1.0f);
    
    uvOut = uv;
    
}