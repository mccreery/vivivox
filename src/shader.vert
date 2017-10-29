varying vec4 camera_pos;
varying vec4 position;

varying float distance;

void main() {
	position = gl_Vertex;
	camera_pos = gl_ModelViewMatrixInverse[3]; 
	camera_pos.y -= 1.75;
	
	distance = length(position - camera_pos) / 2;
	
	gl_TexCoord[0] = gl_MultiTexCoord0;
	
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	gl_FrontColor = gl_Color;
}