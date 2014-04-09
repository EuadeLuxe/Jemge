attribute vec4 a_position;
uniform vec2 dir;
attribute vec2 a_texCoord;
varying vec2 v_texCoords0;
varying vec2 v_texCoords1;
varying vec2 v_texCoords2;
varying vec2 v_texCoords3;
varying vec2 v_texCoords4;

#define FBO_W
FBO_W // TODO
.0 // TODO
#define FBO_H
FBO_H // TODO
.0 // TODO

const vec2 futher = vec2(3.2307692308 / FBO_W, 3.2307692308 / FBO_H );
const vec2 closer = vec2(1.3846153846 / FBO_W, 1.3846153846 / FBO_H );

void main()
{
    vec2 f = futher * dir;
    vec2 c = closer * dir;
    v_texCoords0 = a_texCoord - f;
    v_texCoords1 = a_texCoord - c;
    v_texCoords2 = a_texCoord;
    v_texCoords3 = a_texCoord + c;
    v_texCoords4 = a_texCoord + f;
    gl_Position = a_position;
}