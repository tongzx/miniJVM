/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mini.glfw.utils;

import java.io.UnsupportedEncodingException;
import org.mini.gl.GL;
import static org.mini.gl.GL.glBegin;

/**
 *
 * @author gust
 */
public class Gutil {

    /**
     * fill farr into barr and return barr
     *
     * @param farr
     * @param barr
     * @return
     */
    static public native byte[] f2b(float[] farr, byte[] barr);

    /**
     * load image return opengl GL_TEXTURE_2D id
     *
     * @param pfilename
     * @param para_3slot new int[3] for image w,h,bit depth
     * @return
     */
    public static native int image_load(String pfilename, int[] para_3slot); //const char*, //struct nk_image/*none_ptr*/ 

    /**
     * vec and matrix
     */
    //vec2, vec3, vec4
    static public native float[] vec_add(float[] result, float[] vec1, float[] vec2);

    //vec2, vec3, vec4
    static public native float[] vec_sub(float[] result, float[] vec1, float[] vec2);

    //vec2, vec3, vec4
    static public native float[] vec_scale(float[] result, float[] vec1, float factor);

    //vec2, vec3, vec4
    static public native float vec_mul_inner(float[] vec1, float[] vec2);

    //vec2, vec3, vec4
    static public native float vec_len(float[] vec1);

    //vec2, vec3, vec4
    static public native float[] vec_normal(float[] result, float[] vec1);

    //vec3, vec4
    static public native float[] vec_mul_cross(float[] result, float[] vec1, float[] vec2);

    //vec3, vec4
    static public native float[] vec_reflect(float[] result, float[] vec1, float[] vec2);

    static public native float[] mat4x4_identity(float[] m1);

    static public native float[] mat4x4_dup(float[] r, float[] m1);

    static public native float[] mat4x4_row(float[] r, float[] m1, int row);

    static public native float[] mat4x4_col(float[] r, float[] m1, int col);

    static public native float[] mat4x4_transpose(float[] r, float[] m1);

    static public native float[] mat4x4_add(float[] r, float[] m1, float[] m2);

    static public native float[] mat4x4_sub(float[] r, float[] m1, float[] m2);

    static public native float[] mat4x4_mul(float[] r, float[] m1, float[] m2);

    static public native float[] mat4x4_mul_vec4(float[] r, float[] m1, float[] vec4);

    static public native float[] mat4x4_from_vec3_mul_outer(float[] r, float[] vec31, float[] vec32);

    static public native float[] mat4x4_translate(float[] r, float x, float y, float z);

    static public native float[] mat4x4_translate_in_place(float[] r, float x, float y, float z);

    static public native float[] mat4x4_scale(float[] r, float[] m1, float factor);

    static public native float[] mat4x4_scale_aniso(float[] r, float[] m1, float x, float y, float z);

    static public native float[] mat4x4_rotate(float[] r, float[] m1, float x, float y, float z, float a);

    static public native float[] mat4x4_rotateX(float[] r, float[] m1, float xa);

    static public native float[] mat4x4_rotateY(float[] r, float[] m1, float ya);

    static public native float[] mat4x4_rotateZ(float[] r, float[] m1, float xa);

    static public native float[] mat4x4_invert(float[] r, float[] m1);

    static public native float[] mat4x4_orthonormalize(float[] r, float[] m1);

    static public native float[] mat4x4_ortho(float[] rm, float l, float r, float b, float t, float n, float f);

    static public native float[] mat4x4_frustum(float[] rm, float l, float r, float b, float t, float n, float f);

    static public native float[] mat4x4_perspective(float[] rm, float y_fov, float aspect, float near, float far);

    static public native float[] mat4x4_look_at(float[] rm, float[] vec3_eye, float[] vec3_center, float[] vec3_up);

    static public void gluPerspective(double fov, double aspectRatio, double zNear, double zFar) {
        // 使用glu库函数，需要添加glu.h头文件
        //gluPerspective( fov, aspectRatio, zNear, zFar );

        // 使用OpenGL函数，但是需要添加math.h头文件
        double rFov = fov * 3.14159265 / 180.0;
        GL.glFrustum(-zNear * Math.tan(rFov / 2.0) * aspectRatio,
                zNear * Math.tan(rFov / 2.0) * aspectRatio,
                -zNear * Math.tan(rFov / 2.0),
                zNear * Math.tan(rFov / 2.0),
                zNear, zFar);
    }

    static public void gluLookAt(double eX, double eY, double eZ, double cX, double cY,
            double cZ, double upX, double upY, double upZ) {
        // eye and center are points, but up is a vector
        // 1. change center into a vector:
        // glTranslated(-eX, -eY, -eZ);
        cX = cX - eX;
        cY = cY - eY;
        cZ = cZ - eZ;
        // 2. The angle of center on xz plane and x axis
        // i.e. angle to rot so center in the neg. yz plane
        double a = Math.atan(cZ / cX);
        if (cX >= 0) {
            a = a + Math.PI / 2;
        } else {
            a = a - Math.PI / 2;
        }
        // 3. The angle between the center and y axis
        // i.e. angle to rot so center in the negative z axis
        double b = Math.acos(cY / Math.sqrt(cX * cX + cY * cY + cZ * cZ));
        b = b - Math.PI / 2;
        // 4. up rotate around y axis (a) radians
        double upx = upX * Math.cos(a) + upZ * Math.sin(a);
        double upz = -upX * Math.sin(a) + upZ * Math.cos(a);
        upX = upx;
        upZ = upz;
        // 5. up rotate around x axis (b) radians
        double upy = upY * Math.cos(b) - upZ * Math.sin(b);
        upz = upY * Math.sin(b) + upZ * Math.cos(b);
        upY = upy;
        upZ = upz;
        double c = Math.atan(upX / upY);
        if (upY < 0) {
            // 6. the angle between up on xy plane and y axis
            c = c + Math.PI;
        }
        GL.glRotated(Math.toDegrees(c), 0, 0, 1);
        // up in yz plane
        GL.glRotated(Math.toDegrees(b), 1, 0, 0);
        // center in negative z axis
        GL.glRotated(Math.toDegrees(a), 0, 1, 0);
        // center in yz plane
        GL.glTranslated(-eX, -eY, -eZ);
        // eye at the origin
    }

    static public void drawCood() {
//        GL.glPointSize(5.f);
        float len = 1000f;
        GL.glBegin(GL.GL_LINES);
        GL.glColor3f(1.f, 0, 0);
        GL.glVertex3f(0, 0, 0);
        GL.glVertex3f(len, 0, 0);
        GL.glEnd();
        glBegin(GL.GL_LINES);
        GL.glColor3f(0, 1.f, 0);
        GL.glVertex3f(0, 0, 0);
        GL.glVertex3f(0, len, 0);
        GL.glEnd();
        glBegin(GL.GL_LINES);
        GL.glColor3f(0, 0, 1.f);
        GL.glVertex3f(0, 0, 0);
        GL.glVertex3f(0, 0, len);
        GL.glEnd();
    }

    public static byte[] toUtf8(String s) {
        if (s == null) {
            return null;
        }
        byte[] barr = null;
        try {
            barr = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return barr;
    }

}