varying vec4 camera_pos;
varying vec4 position;

uniform float waveOffset;

void main() {
	gl_TexCoord[0] = gl_MultiTexCoord0;
	
	position = gl_Vertex;
	position.y += sin(position.z / 2 + waveOffset) * cos(position.x / 2 + waveOffset) / 8;
	
	gl_Position = gl_ModelViewProjectionMatrix * position;
	gl_FrontColor = gl_Color;
}