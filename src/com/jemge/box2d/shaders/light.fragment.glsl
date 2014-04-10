#ifdef GL_ES
    precision lowp float;
#define MED mediump
#else
#define MED
#endif

varying vec4 v_color;
uniform int gamma;

void main()
{
    if(gamma == 1)
    {
        gl_FragColor = sqrt(v_color);
    }
    else
    {
        gl_FragColor = v_color;
    }
}