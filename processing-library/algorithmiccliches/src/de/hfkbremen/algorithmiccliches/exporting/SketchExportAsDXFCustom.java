package de.hfkbremen.algorithmiccliches.exporting;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SketchExportAsDXFCustom
        extends PApplet {

    private boolean mRecord;

    public static final int ARRIVED_AT_POSITION = 1;

    public static final int NOT_ARRIVED_YET = 2;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        mRecord = false;
    }

    int checkMouse() {
        return ARRIVED_AT_POSITION;
    }

    public void draw() {
        if (mRecord) {
            beginRaw(DXFExporter.class.getName(), "output.dxf");
        }

        draw(g);

        if (mRecord) {
            endRaw();
            mRecord = false;
        }
    }

    public void keyPressed() {
        if (key == 'r') {
            mRecord = true;
        }
    }

    private void draw(PGraphics pG) {
        pG.background(255);

        for (int i = 0; i < 10; i++) {
            pG.strokeWeight(i * 3);
            pG.line(0, i * height / 10.0f, 0,
                    width, i * height / 10.0f, 0);
        }

        pG.translate(width / 2, height / 2);

        pG.fill(0);
        pG.noStroke();
        pG.sphere(50);

        pG.stroke(0);
        pG.noFill();
        final float mRadius = 400;
        for (int i = 0; i < 2500; i++) {
            PVector v = new PVector();
            v.set(random(1), random(1), random(1));
            v.mult(mRadius);
            pG.strokeWeight(random(0.1f, 2.0f));
            pG.line(0, 0, 0, v.x, v.y, v.z);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchExportAsDXFCustom.class.getName()});
    }
}
