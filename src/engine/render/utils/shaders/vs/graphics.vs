#version 460

layout (location =0) in vec2 pos;

uniform vec2 offset;
uniform mat4 proj;

void main()
{
    gl_Position =  proj * vec4(pos + offset, 0,  1.0f);
}