package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchPerlinNoise extends PApplet {

    private static final int GRID_SIZE = 16;
    private final float mNoiseScale = 0.024f;
    private final ArrayList<MEntity> mEntities = new ArrayList<>();
    private int mCellsX;
    private int mCellsY;
    private PVector[][] mVectorField;
    private float mOffset = 0.0f;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        rectMode(CENTER);

        mCellsX = width / GRID_SIZE;
        mCellsY = height / GRID_SIZE;

        mVectorField = new PVector[mCellsX][mCellsY];
        for (PVector[] mVectorField1 : mVectorField) {
            for (int y = 0; y < mVectorField1.length; y++) {
                mVectorField1[y] = new PVector();
            }
        }
        populateField(mOffset);

        for (int i = 0; i < 3000; i++) {
            mEntities.add(new MEntity());
        }
    }

    public void draw() {
        background(255);

        /* update flow field */
        final float mDeltaTime = 1.0f / frameRate;
        mOffset += 0.05f * mDeltaTime;
        populateField(mOffset);


        /* draw grid */
        if (mousePressed) {
            noFill();
            for (int x = 0; x < mVectorField.length; x++) {
                for (int y = 0; y < mVectorField[x].length; y++) {
                    final PVector v = mVectorField[x][y];
                    pushMatrix();
                    translate(x * GRID_SIZE, y * GRID_SIZE);
                    stroke(0, 7);
                    translate(GRID_SIZE / 2.0f, GRID_SIZE / 2.0f);
                    rect(0, 0, GRID_SIZE, GRID_SIZE);
                    scale(10);
                    strokeWeight(0.1f);
                    stroke(0, 31);
                    line(0, 0, v.x, v.y);
                    popMatrix();
                }
            }
        }

        /* update + draw entities */
        for (MEntity mEntity : mEntities) {
            mEntity.update();
        }
        for (MEntity mEntity : mEntities) {
            noStroke();
            fill(255, 127, 0, 31);
            mEntity.draw(g);
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            mOffset += 0.1f;
            populateField(mOffset);
        }
    }

    private void populateField(float mOffset) {
        for (int x = 0; x < mVectorField.length; x++) {
            for (int y = 0; y < mVectorField[x].length; y++) {
                float mNoise = noise((x * mNoiseScale) + mOffset, (y * mNoiseScale) + mOffset);
                mNoise *= TWO_PI * 2;
                final PVector v = mVectorField[x][y];
                v.x = sin(mNoise);
                v.y = cos(mNoise);
            }
        }
    }

    class MEntity {

        PVector position = new PVector();
        PVector velocity = new PVector();
        PVector acceleration = new PVector();
        float speed = random(150, 300);
        float force = random(600, 900);

        void draw(PGraphics g) {
            g.pushMatrix();
            g.translate(position.x, position.y, position.z);
            g.rotate(atan2(velocity.y, velocity.x));
            g.rect(0, 0, 15, 5);
            g.popMatrix();
        }

        void update() {
            teleport();
            setAccelerationFromForceField();
            move();
        }

        boolean withinBounds(int pCellX, int pCellY) {
            return !(pCellX < 0 || pCellY < 0 || pCellX >= mCellsX || pCellY >= mCellsY);
        }

        private void move() {
            final float mDeltaTime = 1.0f / frameRate;
            acceleration.mult(force);
            PVector mAcc = new PVector().set(acceleration);
            mAcc.mult(mDeltaTime);
            velocity.add(mAcc);
            velocity.normalize();
            velocity.mult(speed);
            PVector mVel = new PVector().set(velocity);
            mVel.mult(mDeltaTime);
            position.add(mVel);
        }

        private void setAccelerationFromForceField() {
            final int mCellX = (int) (position.x / GRID_SIZE);
            final int mCellY = (int) (position.y / GRID_SIZE);
            if (withinBounds(mCellX, mCellY)) {
                PVector v = mVectorField[mCellX][mCellY];
                acceleration.set(v);
            }
        }

        private void teleport() {
            if (position.x < 0) {
                position.x = width;
            }
            if (position.x > width) {
                position.x = 0;
            }
            if (position.y < 0) {
                position.y = height;
            }
            if (position.y > height) {
                position.y = 0;
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchPerlinNoise.class.getName()});
    }
}
