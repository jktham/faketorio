#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;

out vec3 vertTint;
out vec3 vertNormal;
out vec3 vertColor;

uniform vec3 tint;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertTint = tint;
    vertNormal = mat3(transpose(inverse(model))) * normal;
    vertColor = color;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
}
