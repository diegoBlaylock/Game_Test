#DEFINE PROJECTION 0
#version 460

uniform mat4 projectionMatrix;

void project(inout vec4 vector){
	vector = projectionMatrix * vector;
}