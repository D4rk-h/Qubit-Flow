package QuantumCore.model.BlochSphere;

public class VisualConfig {
    private boolean isAnimating;
    private double animationTime;

    public VisualConfig(boolean isAnimating, double animationTime) {
        this.animationTime = animationTime;
        this.isAnimating = isAnimating;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }

    public double getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(double animationTime) {
        this.animationTime = animationTime;
    }
}
