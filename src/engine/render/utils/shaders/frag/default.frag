#version 330

out vec4 fragColor;

layout(location=1) in vec2 uv;

uniform sampler2D texture;

void main()
{
    fragColor = texture(texture, uv);
}
