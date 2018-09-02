varying vec4 camera_pos;
varying vec4 position;

varying float distance;

void main() {
	gl_TexCoord[0] = gl_MultiTexCoord0;
	
	position = gl_Vertex;
	camera_pos = gl_ModelViewMatrixInverse[3]; 
	camera_pos.y -= 1.75;
	
	distance = sin(length(position - camera_pos) / 4) * 2;
	
	position.xyz -= distance;
	
	gl_Position = gl_ModelViewProjectionMatrix * position;
	gl_FrontColor = gl_Color;
}