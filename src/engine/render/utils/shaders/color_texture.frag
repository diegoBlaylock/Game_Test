#version 330

out vec4 fragColor;

layout(location=1) in vec2 uv;

uniform sampler2D texture;
uniform vec4 replacementColor;

void main()
{
    vec4 color = texture(texture, uv);

    if(color.a != 0 && color.rgb != vec3(0,0,0)){
    	fragColor = replacementColor;
    } else {
    	fragColor = vec4(0,0,0,0);
    }

}
