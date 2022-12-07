#version 330

layout (location =0) in vec3 pos;
layout (location =1) in vec2 uv;

layout(location=1) out vec2 uvOut;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 worldMatrix;



void main()
{

    gl_Position =  projectionMatrix * worldMatrix * modelMatrix * vec4(pos, 1.0);
        
    uvOut = uv;
}