package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.laserline.LaserLine2;
import processing.core.PApplet;

import java.util.ArrayList;

public class SketchLaserLine extends PApplet {

    private ArrayList<LaserLine2> mLaserLines = new ArrayList<>();

    public void settings() {
        size(640, 480, P3D);
    }

    public void setup() {
    }

    public void draw() {
        background(0);
        noStroke();

        for (LaserLine2 l : mLaserLines) {
            l.draw(g);
        }

        for (int i = 0; i < mLaserLines.size(); i++) {
            LaserLine2 l = mLaserLines.get(i);
            l.fade_width = sin(radians(frameCount * 4 + i * (360 / mLaserLines.size()))) * 5;
            l.fade_width += 10;
        }
    }

    public void mousePressed() {
        LaserLine2 l = new LaserLine2();
        l.core_color = color(255, 0, 0);
        l.inner_fade_color = color(255, 0, 0, 127);
        l.outer_fade_color = color(255, 0, 0, 0);
        if (mLaserLines.isEmpty()) {
            l.p0.set(width / 2, height / 2);
        } else {
            l.p0.set(mLaserLines.get(mLaserLines.size() - 1).p1);
        }
        l.p1.set(mouseX, mouseY);
        mLaserLines.add(l);
    }

    public static void main(String[] args) {
        PApplet.main(SketchLaserLine.class.getName());
    }

}
