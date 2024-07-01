

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;
in vec3 normal;

out vec4 fragColor;

void main() {
vec4 color = texture(Sampler0, texCoord0);
if (color.a < 0.1) {
    discard;
}
fragColor = color;
}
