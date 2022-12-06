#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec3 aColor;
layout (location = 3) in vec3 iOffset;
layout (location = 4) in vec3 iColor;
layout (location = 5) in vec3 iTint;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 faNormal;
out vec3 faColor;

out vec3 fiColor;
out vec3 fiTint;

void main() {
    faNormal = mat3(transpose(inverse(uModel))) * aNormal;
    faColor = aColor;
    
    fiColor = iColor;
    fiTint = iTint;

    mat4 mvp = uProjection * uView * uModel;
    gl_Position = mvp * vec4(aPosition + iOffset, 1.0);
}
