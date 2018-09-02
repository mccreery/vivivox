uniform sampler2D sceneTex; // 0

uniform float rt_w; // render target width
uniform float rt_h; // render target height
uniform float vx_offset = 0.5;

float offset[3] = float[3]( 0.0, 1.3846153846, 3.2307692308 );
float weight[3] = float[3]( 0.2270270270, 0.3162162162, 0.0702702703 );

void main() 
{
	float z = gl_FragCoord.z / gl_FragCoord.w;
}