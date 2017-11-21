void main(void)
{
 	position = gl_Vertex;
	camera_pos = gl_ModelViewMatrixInverse[3]; 
	camera_pos.y -= 1.75;
	
	gl_TexCoord[0] = gl_MultiTexCoord0;
	
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	gl_FrontColor = gl_Color;
}