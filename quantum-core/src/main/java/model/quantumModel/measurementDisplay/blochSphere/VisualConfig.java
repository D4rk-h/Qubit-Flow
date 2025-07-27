// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package model.quantumModel.measurementDisplay.blochSphere;

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
