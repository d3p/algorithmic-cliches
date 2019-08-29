package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.fluiddynamics.FluidDynamics;
import processing.core.PApplet;

/**
 * http://en.wikipedia.org/wiki/Fluid_Dynamics
 */
public class SketchFluidDynamics2 extends PApplet {

    private boolean showDensity = true;

    private boolean showVelocity = true;

    private FluidDynamics mFluid;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        noStroke();
        textFont(createFont("Courier", 11));

        mFluid = new FluidDynamics(96, 72);
        mFluid.diffusion(0.0003f);
        mFluid.drag(0.995f);

        strokeWeight(0.1f);
    }

    public void draw() {
        background(255);

        float mDeltaTime = 1.0f / frameRate;
        mFluid.update(mDeltaTime);

        if (mousePressed) {
            int x = (mouseX * mFluid.width()) / (width) + 1;
            int y = (mouseY * mFluid.height()) / (height) + 1;

            if (mouseButton == LEFT) {
                final float vX = (mouseX - pmouseX) * 0.01f;
                final float vY = (mouseY - pmouseY) * 0.01f;
                mFluid.setVelocityArea(x, y, vX, vY, 3);
            } else {
                float m = (mouseX - pmouseX) + (mouseY - pmouseY);
                mFluid.setDensityArea(x, y, range(abs(m), 0, 1), 5);
            }
        }

        if (showVelocity) {
            noFill();
            stroke(0, 127, 255, 63);
            pushMatrix();
            scale((float) width / mFluid.width(), (float) height / mFluid.height());
            mFluid.drawVelocity(g);
            popMatrix();
        }

        if (showDensity) {
            noStroke();
            pushMatrix();
            scale((float) width / mFluid.width(), (float) height / mFluid.height());
            mFluid.drawDensity(g, color(255, 127, 0, 225));
            popMatrix();
        }

        noStroke();
        fill(127);
        text("VISCOSITY: " + mFluid.viscosity() * 100, 10, 12);
        text("DIFFUSION: " + mFluid.diffusion() * 100, 10, 24);
        text("FPS      : " + (int) frameRate, 10, 36);
    }

    public void keyPressed() {

        if (key == 'v') {
            showVelocity = !showVelocity;
        }

        if (key == 'd') {
            showDensity = !showDensity;
        }

        if (key == 'r') {
            mFluid.reset();
        }

        if (key == '+') {
            mFluid.viscosity(mFluid.viscosity() + 0.00001f);
        }
        if (key == '-') {
            mFluid.viscosity(mFluid.viscosity() - 0.00001f);
        }
        if (key == '*') {
            mFluid.diffusion(mFluid.diffusion() + 0.00001f);
        }
        if (key == '_') {
            mFluid.diffusion(mFluid.diffusion() - 0.00001f);
        }
        mFluid.viscosity(max(mFluid.viscosity(), 0.0f));
        mFluid.diffusion(max(mFluid.diffusion(), 0.0f));
    }

    private float range(float f, float minf, float maxf) {
        return Math.max(Math.min(f, maxf), minf);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchFluidDynamics2.class.getName()});
    }
}
