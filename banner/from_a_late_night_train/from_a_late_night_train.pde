import java.util.*;

class Particle {
  float x, y;
  float old_x, old_y;
  float dx, dy;
  float r;

  Particle() {
    x = -100;
  }

  void init(float x, float y, float dx, float dy, float r) {
    this.x = this.old_x = x;
    this.y = this.old_y = y;
    this.dx = dx;
    this.dy = dy;
    this.r = r;
  }

  void draw() {
    noStroke();
    fill(255, 255, 255);
    for (float p = 0.0; p <= 1.0; p += 0.01) {   
      ellipse(x + dx * p, y + dy * p, r, r);
    }

    old_x = x;
    old_y = y;
    x += dx;
    y += dy;
  }
}

class Emitter {
  Vector<Particle> v = new Vector<Particle>();
  int width;
  int height;

  Emitter(int w, int h) {
    width = w;
    height = h;

    for (int i = 0; i < 50; ++i) {
      v.add(new Particle());
    }
  }

  void draw() {
    for (int i = 0; i < v.size (); ++i) {
      Particle p = v.get(i);
      p.draw();
      // check
      if (p.x < 0) {
        float d = random(0.9) + 0.1;
        float r = 5 * d;
        float dx = 20 * d;
        p.init(width, d * height, -dx, 0, r);
      }
    }
  }
};

Emitter emitter;
PImage img;

void setup() {
  size(1024, 500);
  background(0);
  emitter = new Emitter(width, height);
  img = loadImage("icon.png");
}

void draw() {
  noStroke();
  fill(0, 0, 0, 20);
  rect(0, 0, width, height);


  emitter.draw();
  image(img, 0, 0, 300, 300);
}

