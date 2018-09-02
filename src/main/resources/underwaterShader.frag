uniform sampler2D color_texture;

varying float distance;

void main() {
	gl_FragColor = texture2D(color_texture,  gl_TexCoord[0].st) * gl_Color;
}