package com.example.YuCeClient.widget.custom_viewgroup;

/**
 * Created by sreay on 15-9-15.
 */

/**
 * ViewContainer中的孩子必须实现该接口*
 */
public interface IViewContainerItem {
    /**
     * 得到宽度的measureModel*
     */
    public MeasureModel getWidthMeasureModel();

    /**
     * 得到高度的measureModel*
     */
    public MeasureModel getHeightMeasureMoldel();

    /**
     * 高度是宽度的几倍，等比例*
     */
    public int getMultiple();
}
