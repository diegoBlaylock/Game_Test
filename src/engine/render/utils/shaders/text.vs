#version 330

layout (location =0) in vec3 pos;
layout (location =1) in vec2 uv;

layout(location=1) out vec2 uvOut;

uniform vec2 offset;
uniform float scale;

void main()
{
	vec3 scaled = pos * scale;
    gl_Position = vec4(scaled.x + offset.x, scaled.y + offset.y, scaled.z,  1.0f);
    
    uvOut = uv;
    
}