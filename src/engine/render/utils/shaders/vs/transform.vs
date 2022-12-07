#DEFINE TRANSFORM 0
#version 460

uniform mat4 modelMatrix;
uniform mat4 worldMatrix;

void transform(inout vec4 vector){
	vector = projectionMatrix * vector;
	
	vector = worldMatrix * modelMatrix * vector;
}