#version 330 core

in vec3 faNormal;
in vec3 faColor;

in vec3 fiColor;
in vec3 fiTint;

out vec4 fragColor;

void main() {
    vec3 normal = normalize(faNormal);
    vec3 lightDir = normalize(vec3(-1.0, -2.0, 3.0));
    vec3 lightColor = vec3(1.0, 1.0, 1.0);

    vec3 ambient = lightColor * 0.4;
    vec3 diffuse = lightColor * max(dot(normal, lightDir), 0.0) * 0.5;

    vec3 baseColor = faColor;
    if (fiColor.x >= 0.0) {
        baseColor = baseColor * fiColor;
    }
    if (fiTint.x >= 0.0) {
        baseColor = mix(baseColor, fiTint, 0.5);
    }
    vec3 color = baseColor * (ambient + diffuse);
    fragColor = vec4(color, 1.0);
}
