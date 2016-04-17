package com.example.YuCeClient.widget.custom_viewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.example.YuCeClient.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sreay on 15-9-15.
 */

/**
 * MatchParent填充父亲或者横方向上填充父亲剩余的空间，可以设置padding,设置分割线(分割线不包括边上的分割线)*
 */
public class SuperViewContainer extends ViewGroup {
    /**
     * 二维数组维持不规则矩形的填充*
     */
    private ArrayList<int[]> map = new ArrayList<int[]>();

    /**
     * 存储view和对应索引的hashmap*
     */
    private HashMap<Integer, View> views = new HashMap<Integer, View>();

    /**
     * 存储Rect和对应索引的hashmap*
     */
    private HashMap<Integer, Rect> rects = new HashMap<Integer, Rect>();

    /**
     * 计算孩子权重的时候，哪些已经计算过*
     */
    private List<Integer> alreadyCal = new ArrayList<Integer>();

    /**
     * 分割线的宽度*
     */
    private int lineWidth = 0;

    /**
     * 横方向分割线的最大的数量*
     */
    int maxRowBorderCount = 0;

    /**
     * 竖直方向上分割线的最大的数量*
     */
    int maxCloBorderCount = 0;

    /**
     * 横方向上整个的权重,也是每一列多少个孩子*
     */
    private int totalWeight = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SuperViewContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributeSet(attrs);
    }

    public SuperViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(attrs);
    }

    public SuperViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(attrs);
    }

    public SuperViewContainer(Context context) {
        super(context);
        initAttributeSet(null);
    }

    /**
     * 初始化atts参数*
     */
    private void initAttributeSet(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SuperViewContainer, 0, 0);
        totalWeight = typedArray.getInteger(R.styleable.SuperViewContainer_hor_total_weight, 0);
        if (totalWeight <= 0) {
            throw new IllegalArgumentException("totalWieght <=0");
        }
        float dpValue = typedArray.getFloat(R.styleable.SuperViewContainer_border_line_width, 0);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        this.lineWidth = (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置横方向上的totalWeight*
     */
    public void setTotalWeight(int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException("totalWieght <=0");
        }
        this.totalWeight = totalWeight;
        postInvalidate();
    }

    /**
     * 设置分割线的宽度*
     */
    public void setLineWidth(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        this.lineWidth = (int) (dpValue * scale + 0.5f);
        postInvalidate();
    }

    /**
     * 测量每个孩子的大小以及ViewContainer的高度*
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(widthMeasureSpec, 0);
            return;
        }
        /**检查权重是否大于0**/
        if (totalWeight <= 0) {
            super.onMeasure(widthMeasureSpec, 0);
            return;
        }
        /**将viewgroup的padding计算进去**/
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        /**容器的总宽度**/
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);
        Log.d("xiaoyu", "widthValue--" + widthValue);
        /**检查每一个孩子的MeasureModel组合起来的合法性**/
        checkAllChildLegitimate();
        /**初始化map和views**/
        initMapAndHashMap();
        /**每个孩子横向的宽度，按照权重来算，matchparent的单独计算**/
        float childWeightWidth = (widthValue - paddingLeft - paddingRight - (maxRowBorderCount) * lineWidth) / totalWeight;
        /**因为除不尽的缘故，所以右边会有间距，但是两边需要对齐，不能左对齐**/
        int realWidth = (int) (childWeightWidth * totalWeight + maxRowBorderCount * lineWidth + paddingLeft + paddingRight);
        int horOffset = (widthValue - realWidth) / 2;
        /**循环计算每个孩子的大小以及ViewContainer的高度**/
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < totalWeight; j++) {
                /**从map中得到view索引**/
                int position = map.get(i)[j];
                if (alreadyCal.contains(position)) {
                    continue;
                }
                /**孩子的范围**/
                int left = 0, top = 0, right = 0, bottom = 0;
                /**根据索引从hashmap中得到view**/
                View currentChild = views.get(position);
                if (currentChild != null) {
                    /**根据topChild初始化top**/
                    View topChild = getTopView(i, j);
                    if (topChild == null) {
                        top = paddingTop;
                    } else {
                        int previous = getTopViewPosition(i, j);
                        top = rects.get(previous).bottom + lineWidth;
                    }
                    /**根据leftChild初始化left**/
                    View leftChild = getLeftview(i, j);
                    if (leftChild == null) {
                        left = paddingLeft + horOffset;
                    } else {
                        int previous = getLeftviewPosition(i, j);
                        left = rects.get(previous).right + lineWidth;
                    }
                    /**根据MeasureModel来计算孩子的宽度和高度**/
                    IViewContainerItem iCurrentChild = (IViewContainerItem) currentChild;
                    MeasureModel widthMeasureModel = iCurrentChild.getWidthMeasureModel();
                    /**计算right**/
                    if (widthMeasureModel.getPattern() == MeasureModel.Pattern.MATCHPARENT) {
                        /**循环，查看该matchparent占该行的比例**/
                        int[] currentRow = map.get(i);
                        int count = 0;
                        for (int k = 0; k < totalWeight; k++) {
                            if (currentRow[k] == position) {
                                count++;
                            }
                        }
                        right = (int) (left + childWeightWidth * count + (count - 1) * lineWidth);
                    } else if (widthMeasureModel.getPattern() == MeasureModel.Pattern.WEIGHT) {
                        right = (int) (left + childWeightWidth);
                    }
                    /**计算bottom**/
                    MeasureModel heightMeasureModel = iCurrentChild.getHeightMeasureMoldel();
                    if (heightMeasureModel.getPattern() == MeasureModel.Pattern.WEIGHT) {
                        if (widthMeasureModel.getPattern() == MeasureModel.Pattern.MATCHPARENT) {
                            bottom = (int) (top + childWeightWidth * heightMeasureModel.getValue() + lineWidth * (iCurrentChild.getMultiple() - 1));
                        } else {
                            bottom = (int) (top + (right - left) * (heightMeasureModel.getValue() / widthMeasureModel.getValue()) + lineWidth * (iCurrentChild.getMultiple() - 1));
                        }
                    } else if (heightMeasureModel.getPattern() == MeasureModel.Pattern.DIMENSION) {
                        final float scale = getContext().getResources().getDisplayMetrics().density;
                        int heightValue = (int) (heightMeasureModel.getValue() * scale + 0.5f);
                        bottom = (int) (top + heightValue);
                    }
                    /**将rect加入hashmap**/
                    Rect childRect = new Rect(left, top, right, bottom);
                    rects.put(position, childRect);
                    int widthMea = MeasureSpec.makeMeasureSpec(childRect.width(), MeasureSpec.EXACTLY);
                    int heiMea = MeasureSpec.makeMeasureSpec(childRect.height(), MeasureSpec.EXACTLY);
                    currentChild.measure(widthMea, heiMea);
                }
                /**将已经计算过的索引加入计算过的列表中**/
                alreadyCal.add(position);
            }
        }

        /**计算出总高度**/
        /**清除alreadyCal**/
        alreadyCal.clear();
        /**set竖直方向上除去map中重复的数据**/
        Set<Integer> set = new HashSet<Integer>();
        /**初始化高度数组，找出其中最高的那个**/
        int[] heights = new int[totalWeight];
        /**上面计算完了孩子的所有的大小，计算Viewcontainer的总高度**/
        for (int i = 0; i < totalWeight; i++) {
            for (int j = 0; j < map.size(); j++) {
                int[] row = map.get(j);
                int position = row[i];
                if (position != -1) {
                    if (alreadyCal.contains(position)) {
                        continue;
                    }
                    Rect rect = rects.get(position);
                    if (rect != null) {
                        heights[i] = heights[i] + rect.height();
                    }
                    alreadyCal.add(position);
                    set.add(position);
                }
            }
            /**添加分割线的高度**/
            heights[i] = heights[i] + (set.size() - 1) * lineWidth;
            alreadyCal.clear();
            set.clear();
        }

        /**找出heights中高度最大的作为viewgroup的高度**/
        int maxHeight = 0;
        for (int i = 0; i < totalWeight; i++) {
            if (maxHeight < heights[i]) {
                maxHeight = heights[i];
            }
        }
        setMeasuredDimension(widthValue, maxHeight + paddingTop + paddingBottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**摆放孩子的位置**/
        Set<Integer> positions = views.keySet();
        for (Integer position : positions) {
            View child = views.get(position);
            Rect rect = rects.get(position);
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    /**
     * 得到map中该view上面的一个view*
     */
    private View getTopView(int i, int j) {
        int tempI = i - 1;
        if (tempI < 0) {
            return null;
        } else {
            int position = map.get(tempI)[j];
            return views.get(position);
        }
    }

    /**
     * 得到map中该View上面的view的position*
     */
    private int getTopViewPosition(int i, int j) {
        int tempI = i - 1;
        if (tempI < 0) {
            return -1;
        } else {
            int position = map.get(tempI)[j];
            return position;
        }
    }

    /**
     * 得到map中该view左边的一个view*
     */
    private View getLeftview(int i, int j) {
        int tempJ = j - 1;
        if (tempJ < 0) {
            return null;
        } else {
            int position = map.get(i)[tempJ];
            return views.get(position);
        }
    }

    /**
     * 得到map中该view左边的一个view的position*
     */
    private int getLeftviewPosition(int i, int j) {
        int tempJ = j - 1;
        if (tempJ < 0) {
            return -1;
        } else {
            int position = map.get(i)[tempJ];
            return position;
        }
    }

    /**
     * 检查每一个孩子是否合法*
     */
    private void checkAllChildLegitimate() {
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i) instanceof IViewContainerItem) {
                    IViewContainerItem iViewContainerItem = (IViewContainerItem) getChildAt(i);
                    MeasureModel widthMeasureModel = iViewContainerItem.getWidthMeasureModel();
                    MeasureModel heightMeasureModel = iViewContainerItem.getHeightMeasureMoldel();
                    /**宽度和高度只能有一个是matchparent**/
                    if (widthMeasureModel.getPattern() == MeasureModel.Pattern.MATCHPARENT && heightMeasureModel.getPattern() == MeasureModel.Pattern.MATCHPARENT) {
                        throw new IllegalArgumentException("width or height must has one not be matchparent");
                    }
                    /**为了对其，宽度不能是Dimension**/
                    if (widthMeasureModel.getPattern() == MeasureModel.Pattern.DIMENSION) {
                        throw new IllegalArgumentException("width should not be dimension");
                    }
                    /**这里先不允许高度为matchparent**/
                    if (heightMeasureModel.getPattern() == MeasureModel.Pattern.MATCHPARENT) {
                        throw new IllegalArgumentException("height should not be matchparent");
                    }
                    /**如果宽度是权重，高度是dimension，则不合法，为了对齐显示**/
                    if (widthMeasureModel.getPattern() == MeasureModel.Pattern.WEIGHT && heightMeasureModel.getPattern() == MeasureModel.Pattern.DIMENSION) {
                        throw new IllegalArgumentException("width is matchparent and height is dimension,is not right");
                    }
                } else {
                    throw new IllegalArgumentException("childView must instance IViewContainerItem");
                }
            }
        }
    }

    /**
     * 初始化二维数组map，记录每一个View在ViewGroup中占的位置*
     */
    private void initMapAndHashMap() {
        /**必须初始化totalWeight**/
        if (totalWeight <= 0) {
            return;
        }
        /**清除map中的数据**/
        if (map != null) {
            map.clear();
        }
        /**清除已经计算过的索引**/
        if (alreadyCal != null) {
            alreadyCal.clear();
        }
        /**清除hashmap中的数据**/
        if (views != null && !views.isEmpty()) {
            views.clear();
        }
        /**当前行**/
        int currentRow = 0;
        /**每一列当前的数量**/
        int currentColCount = 0;
        /**初始化map和views**/
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                IViewContainerItem iView = (IViewContainerItem) view;
                /**计算换行**/
                if (iView.getWidthMeasureModel().getPattern() == MeasureModel.Pattern.MATCHPARENT) {
                    /**当view的宽度是matchparent的时候，占据该行剩余的宽度**/
                    views.put(i, view);
                    int tempCurrentRow = currentRow;
                    /**查看该行中是否有其他view**/
                    if (tempCurrentRow >= map.size()) {/**如果是新的一行**/
                        int[] row = new int[totalWeight];
                        Arrays.fill(row, -1);
                        for (int j = 0; j < totalWeight; j++) {
                            row[j] = i;
                        }
                        map.add(row);
                    } else {/**可以获取到**/
                        int[] row = map.get(tempCurrentRow);
                        /**判断当前currentColCount有没有值，如果有值，说明已经被其他view占位,如果占位则寻找没有占位的currentColCount**/
                        if (row[currentColCount] != -1) {/**已经被占位**/
                            while (true) {
                                currentColCount++;
                                if (row[currentColCount] == -1) {
                                    for (int j = currentColCount; j < totalWeight; j++) {
                                        row[j] = i;
                                    }
                                    break;
                                }
                            }
                        } else {/**没有被占位**/
                            for (int j = currentColCount; j < totalWeight; j++) {
                                row[j] = i;
                            }
                        }
                    }
                    currentRow++;
                    currentColCount = 0;
                } else {
                    /**高度是宽度的几倍，这样可以初始化map的格子**/
                    int multiple = iView.getMultiple();
                    int tempCurrentRow = currentRow;
                    for (int j = 0; j < multiple; j++) {
                        /**从列表中获得当前列，如果获取不到则加入一个**/
                        if (tempCurrentRow >= map.size()) {
                            int[] row = new int[totalWeight];
                            Arrays.fill(row, -1);
                            map.add(row);
                            row[currentColCount] = i;
                        } else {/**可以获取到**/
                            int[] row = map.get(tempCurrentRow);
                            /**判断当前currentColCount有没有值，如果有值，说明已经被其他view占位,如果占位则寻找没有占位的currentColCount**/
                            if (row[currentColCount] != -1) {/**已经被占位**/
                                while (true) {
                                    currentColCount++;
                                    if (row[currentColCount] == -1) {
                                        row[currentColCount] = i;
                                        break;
                                    }
                                }
                            } else {/**没有被占位**/
                                row[currentColCount] = i;
                            }
                        }
                        tempCurrentRow++;
                    }
                    views.put(i, view);
                    currentColCount++;
                    if (currentColCount >= totalWeight) {
                        currentRow++;
                        currentColCount = 0;
                    }
                }
            }
        }
        /**地图填充完毕后，需要计算每一行的分割线最多的行和列，因为需要根据最多的行和列计算child的大小**/
        /**统计分割线最多的行,利用set去除重复**/
        Set<Integer> set = new HashSet<Integer>();
        maxRowBorderCount = 0;
        maxCloBorderCount = 0;

        for (int i = 0; i < map.size(); i++) {
            int[] row = map.get(i);
            for (int j = 0; j < totalWeight; j++) {
                if (row[j] != -1) {
                    set.add(row[j]);
                }
            }
            if (maxRowBorderCount < set.size() - 1) {
                maxRowBorderCount = set.size() - 1;
            }
            set.clear();
        }

        /**统计分割线最多的列，利用set去除重复**/
        set.clear();
        for (int i = 0; i < totalWeight; i++) {
            for (int j = 0; j < map.size(); j++) {
                int[] row = map.get(j);
                if (row[i] != -1) {
                    set.add(row[i]);
                }
            }
            if (maxCloBorderCount < set.size() - 1) {
                maxCloBorderCount = set.size() - 1;
            }
            set.clear();
        }
    }
}
