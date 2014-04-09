#ifdef GL_ES
precision lowp float;
#define MED mediump
#else
#define MED
#endif

uniform sampler2D u_texture;
varying MED vec2 v_texCoords0;
varying MED vec2 v_texCoords1;
varying MED vec2 v_texCoords2;
varying MED vec2 v_texCoords3;
varying MED vec2 v_texCoords4;
const float center = 0.2270270270;
const float close  = 0.3162162162;
const float far    = 0.0702702703;

void main()
{
    useCenterLight() // TODO
}