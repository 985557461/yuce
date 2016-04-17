package com.example.YuCeClient.widget.custom_viewgroup;

/**
 * Created by sreay on 15-9-15.
 */

/**
 * 这个类用来初始化ViewContainerItem的宽度和高度*
 */
public class MeasureModel {
    /**
     * 三种模式：填充父亲，具体的值，权重*
     */
    public enum Pattern {
        MATCHPARENT, DIMENSION, WEIGHT
    }

    /**
     * 模式*
     */
    private Pattern pattern;
    /**
     * 值*
     */
    private float value;

    private MeasureModel() {
    }

    public MeasureModel(Pattern pattern, float value) {
        if (pattern == null) {
            throw new NullPointerException("pattern is null");
        }
        this.pattern = pattern;
        this.value = value;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public float getValue() {
        return value;
    }
}
