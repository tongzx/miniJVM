/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mini.gui;

import static org.mini.gui.GObject.LEFT;
import static org.mini.nanovg.Gutil.toUtf8;
import static org.mini.nanovg.Nanovg.NVG_ALIGN_LEFT;
import static org.mini.nanovg.Nanovg.NVG_ALIGN_MIDDLE;
import static org.mini.nanovg.Nanovg.NVG_ALIGN_TOP;
import static org.mini.nanovg.Nanovg.nvgFillColor;
import static org.mini.nanovg.Nanovg.nvgFontFace;
import static org.mini.nanovg.Nanovg.nvgFontSize;
import static org.mini.nanovg.Nanovg.nvgTextAlign;
import static org.mini.nanovg.Nanovg.nvgTextBoxBoundsJni;
import static org.mini.nanovg.Nanovg.nvgTextBoxJni;
import static org.mini.nanovg.Nanovg.nvgTextJni;

/**
 *
 * @author gust
 */
public class GLabel extends GObject {

    String text;
    byte[] text_arr;
    char preicon;

    int align = NVG_ALIGN_LEFT | NVG_ALIGN_TOP;

    public GLabel() {

    }

    public GLabel(String text, int left, int top, int width, int height) {
        this(text, (float) left, top, width, height);
    }

    public GLabel(String text, float left, float top, float width, float height) {
        setText(text);
        setLocation(left, top);
        setSize(width, height);
    }

    public int getType() {
        return TYPE_LABEL;
    }

    public void setAlign(int ali) {
        align = ali;
    }

    public final void setText(String text) {
        this.text = text;
        text_arr = toUtf8(text);
    }

    public void setIcon(char icon) {
        preicon = icon;
    }

    /**
     *
     * @param vg
     * @return
     */
    public boolean update(long vg) {
        float x = getX();
        float y = getY();
        float w = getW();
        float h = getH();

        drawText(vg, x, y, w, h);
        return true;
    }

    void drawLabel(long vg, float x, float y, float w, float h) {
        //NVG_NOTUSED(w);
        nvgFontSize(vg, GToolkit.getStyle().getTextFontSize());
        nvgFontFace(vg, GToolkit.getFontWord());
        nvgFillColor(vg, GToolkit.getStyle().getTextFontColor());

        nvgTextAlign(vg, align);
        nvgTextJni(vg, x, y + h * 0.5f, text_arr, 0, text_arr.length);

    }

    void drawText(long vg, float x, float y, float w, float h) {

        nvgFontSize(vg, GToolkit.getStyle().getTextFontSize());
        nvgFillColor(vg, GToolkit.getStyle().getTextFontColor());
        nvgFontFace(vg, GToolkit.getFontWord());

        nvgTextAlign(vg, align);
        float[] text_area = new float[]{x + 2f, y + 2f, w - 4f, h - 4f};
        float dx = text_area[LEFT];
        float dy = text_area[TOP];

        if (text_arr != null) {
            float[] bond = new float[4];
            nvgTextBoxBoundsJni(vg, text_area[LEFT], text_area[TOP], text_area[WIDTH], text_arr, 0, text_arr.length, bond);
            bond[WIDTH] -= bond[LEFT];
            bond[HEIGHT] -= bond[TOP];
            bond[LEFT] = bond[TOP] = 0;

            if (bond[HEIGHT] > text_area[HEIGHT]) {
                dy -= bond[HEIGHT] - text_area[HEIGHT];
            }
            nvgTextBoxJni(vg, dx, dy, text_area[WIDTH], text_arr, 0, text_arr.length);
        }
    }

}
