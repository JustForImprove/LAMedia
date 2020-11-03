package com.la.mymedia.customview.SmartRecyclerViewLayoutManager;

import android.graphics.Path;
import android.util.Log;
import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SmartLayoutManager extends RecyclerView.LayoutManager {

    private int mOrientation = 1;
    private int mItemCountInScreen = 0;
    private int itemOffset = 0;
    private Keyframes keyframes = null;
    private float mOffsetY = 0;
    private float mOffsetX = 0;
    private int mFirstVisibleItemPos = 0;
    private boolean isLoopScrollMode = true;
    private float[] mScaleRatio;// = new float[3];

    /**
 * @param path        目标路径
 * @param itemOffset  Item间距
 * @param orientation 滑动方向
 */

    // 先获取Path路径对象，并初始化路径坐标存储对象
    //
    public SmartLayoutManager(Path path, int itemOffset, @RecyclerView.Orientation int orientation, float[] ratio) {
        this.mOrientation = orientation;
        this.itemOffset = itemOffset;
        this.mScaleRatio = ratio;
        updatePath(path);
    }
    //获取Keyframes对象，并根据路径算出的路径长度计算出能出现几个Item
    private void updatePath(Path path) {
        // 异常判断
        if (path == null){
            throw new IllegalArgumentException("路径不能为空");
        }else if (itemOffset == 0) {
            throw new IllegalArgumentException("间距不能为0，会导致控件重叠");
        }
        // 获取Keyframes对象
        keyframes = new Keyframes(path);
        //计算出这个Path最多能同时出现几个Item
        mItemCountInScreen = keyframes.getPathLength()/ itemOffset + 1; //

        // 请求重新计算布局，最后会被封装到Runnable，保存到待执行队列中，等待VSYNC信号
        // 接到信号后执行performTraversals()，开始view的绘制流程
        requestLayout();
    }


    @Override
    public boolean canScrollVertically() {
        //设置了滑动方向是垂直，才能接受垂直滚动事件
        return mOrientation == RecyclerView.VERTICAL;
    }

    @Override
    public boolean canScrollHorizontally() {
        //设置了滑动方向是水平，才能接受水平滚动事件
        return mOrientation == RecyclerView.HORIZONTAL;
    }
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }

    private void initNeedLayoutItems(ArrayList<PosTan> needLayoutItems, int itemCount) {
        // 声明可见判定变量，判定值小于0时不可见，进行回收
        float currentDis;
        for(int i = 0; i < itemCount; i++){
            // 滚动偏移量是当前scroll滑动到的位置， 用于让item正确的出现在屏幕上
            // 判定值等于item数 * 间隔值 - 滚动偏移量
            currentDis = i * itemOffset - getScrollOffset();
            if (currentDis >= 0){
                //得到第一个可见的position
                mFirstVisibleItemPos = i;
                break;
            }
        }

        int endIndex = mFirstVisibleItemPos + mItemCountInScreen;

        if (endIndex > getItemCount()){
            endIndex = getItemCount(); // 防止溢出
        }
        float fraction;
        PosTan posTan;
        // 从第一个可见的item开始，计算
        for (int i = mFirstVisibleItemPos; i < endIndex; i++){
            currentDis = i * itemOffset - getScrollOffset();
            fraction = currentDis / keyframes.getPathLength();
            posTan = keyframes.getValue(fraction);
            if (posTan == null){
                continue;
            }
            // 添加如需要布局的list
            posTan.setIndex(i);
            needLayoutItems.add(posTan);
        }

    }

    private float getScrollOffset() {
        return mOrientation == RecyclerView.VERTICAL ? mOffsetY : mOffsetX;
    }
    // view绘制流程开始后实际执行布局计算处

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e("tttsfsdfsttttt","ddddddddddddd");
        if (state.getItemCount() == 0){
            removeAndRecycleAllViews(recycler);
            return;
        }
        // 可进一步了解RecyclerView的回收机制
        // 先将item从 RecyclerView上分离并回收到recycler，以便于执行我们自己的布局方法
        detachAndScrapAttachedViews(recycler);
        // item抽象位置的存储集合
        ArrayList<PosTan> needLayoutItems;// = new ArrayList<>();
        //计算并获取我们需要布局的items
        //initNeedLayoutItems(needLayoutItems, state.getItemCount());
        Log.e("ttttttttttt","ddddddddddddd");
        needLayoutItems = getNeedLayoutItems();
        //确保needLayoutItems与mKeyframes不为空
        if (needLayoutItems.isEmpty() || keyframes == null){
            throw new NullPointerException("布局子项为空或路径有问题");
        }
        // 重新布局item
        onLayout(recycler, needLayoutItems);

    }

    private void onLayout(RecyclerView.Recycler recycler, ArrayList<PosTan> needLayoutItems) {
        // 声明布局子项坐标
        int x, y;
        // 声明子项
        View item;
        for (PosTan tmp: needLayoutItems){
            // 通过item抽象位置的当前索引获取item
            item = recycler.getViewForPosition(tmp.index);
            //
            addView(item);
            // 测量item，当然，也不是每次都会调用measure方法进行测量的，
            // 它里面会判断，如果已经测量过，而且当前尺寸又没有收到更新的通知，就不会重新测量。
            // 为什么传0？
            measureChild(item, 0,0);
            //Path线条在View的中间
            x = (int) tmp.x - getDecoratedMeasuredWidth(item) / 2;
            y = (int) tmp.y - getDecoratedMeasuredHeight(item) / 2;
            //进行布局
            layoutDecorated(item, x, y, x + getDecoratedMeasuredWidth(item), y + getDecoratedMeasuredHeight(item));
            //旋转item
            item.setRotation(tmp.getChildAngle());
            if (mScaleRatio != null) {
                //根据item当前位置获取到对应的缩放比例
                float scale = getScale(tmp.fraction);
                //设置缩放
                item.setScaleX(scale);
                item.setScaleY(scale);
            }
        }


    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (keyframes == null){
            return 0;
        }
        detachAndScrapAttachedViews(recycler);
        float lastOffset = mOffsetY;
        updateOffsetY(dy);
        relayoutChildren(recycler, state);
        return lastOffset == mOffsetY?0:dy;
    }

    private void updateOffsetY(float offsetY) {
        // 更新offsetY
        mOffsetY = mOffsetY + offsetY;
        // 得到路径总长度
        int pathLength = keyframes.getPathLength();
        // item总长度
        int itemLength = getItemCount() * itemOffset;
        // item总长度相对于路径总长度多出来的部分
        int overflowLength = itemLength - pathLength;
        // 无限循环
        if(isSatisfiedLoopScroll()){
            //如果全部item即将向上滑出屏幕，这个时候如果是无限循环的话，
            //那么就是会显示第0个item，所以我们可以偷梁换柱，把早已滑出屏幕的第0个item，
            //移回屏幕中，不用担心有什么副作用，这就像一个矩形，旋转了361度和旋转了1度是一样的道理
            if (mOffsetY > itemLength) {
                mOffsetY %= itemLength;
                //因为是向前偏移了一个item的距离
                mOffsetY -= itemOffset;
            } else if (mOffsetY <= -pathLength) {
                //原理同上
                mOffsetY += itemLength;
                mOffsetY += itemOffset;
            }
        }else {
            if (mOffsetY < 0) {
                //避免第一个item脱离顶部向下滚动
                mOffsetY = 0;
            } else if (mOffsetY > overflowLength) {//滑动到底部，并且最后一个item即将脱离底部时
                //如果列表能滚动的话，则直接设置为可滑动的最大距离，避免最后一个item向上移
                if (itemLength > pathLength) {
                    mOffsetY = overflowLength;
                } else {
                    //如果列表内容很少，不用滚动就能显示完的话，就不更新offset
                    //那为什么这里是减呢？因为最上面执行了一句+=，所以现在这样做是抵消第一句的操作。

                    mOffsetY -= offsetY;
                }
            }
        }
    }

    private boolean isSatisfiedLoopScroll() {
        //checkKeyframes();
        int pathLength = keyframes.getPathLength();
        int itemLength = getItemCount() * itemOffset;
        return isLoopScrollMode && itemLength - pathLength > itemOffset;
    }

    private void relayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        ArrayList<PosTan> needLayoutItems;// = new ArrayList<>();
        //获取需要布局的items
        //initNeedLayoutItems(needLayoutItems, state.getItemCount());
        needLayoutItems = getNeedLayoutItems();

        //判断一下状态
        if (needLayoutItems.isEmpty() || keyframes == null) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        //recycleChildren(recycler ,needLayoutItems);
        //开始布局
        onLayout(recycler, needLayoutItems);
        recycleChildren(recycler, needLayoutItems); // 未执行回收工作加载7000多个就会崩，执行可加载无限多个。。。。。。

    }

    private void recycleChildren(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return;
        }
        for (int i = startIndex; i <= endIndex; i++) {
            final View view = recycler.getViewForPosition(i);
            if (view != null) {
                removeView(view);
                recycler.recycleView(view);
            }
        }


    }

    private void recycleChildren(RecyclerView.Recycler recycler, List<PosTan> needLayoutDataList) {
        //item总数
        int itemCount = getItemCount();
        //列表中第一个能看到的item索引
        //不用担心IndexOutOfBoundsException的问题，
        //因为我们在上面已经做了判断，如果list为空，直接return了，不会执行到这里
        int firstIndex = needLayoutDataList.get(0).index;
        //最后一个能看到的item索引
        int lastIndex = needLayoutDataList.get(needLayoutDataList.size() - 1).index;
        //前面那一段的起始和结束索引
        int forwardStartIndex, forwardEndIndex;
        //后面一段的起始和结束索引
        int backwardStartIndex, backwardEndIndex;
        //等下还需判断是否需要回收
        boolean needRecyclerForward = false, needRecyclerBackward = false;

        //排除第一个，所以-1
        forwardEndIndex = firstIndex - 1;
        //回收的范围 = 列表中能同时显示的数量
        forwardStartIndex = forwardEndIndex - mItemCountInScreen;
        //排除最后一个，所以+1
        backwardStartIndex = lastIndex + 1;
        //回收的范围 = 列表中能同时显示的数量
        backwardEndIndex = backwardStartIndex + mItemCountInScreen;

        //如果第一个显示的item索引为0，就不用回收了
        if (firstIndex > 0) {
            if (forwardStartIndex < 0) {
                forwardStartIndex = 0;
            }
            //标记需要回收
            needRecyclerForward = true;
        }
        //如果adapter数据集中最后一个item正在显示，也不用回收
        if (lastIndex < itemCount - 1) {
            if (backwardEndIndex >= itemCount) {
                backwardEndIndex = itemCount - 1;
            }
            //标记需要回收
            needRecyclerBackward = true;
        }
        //回收前半段
        if (needRecyclerForward) {
            recycleChildren(recycler, forwardStartIndex, forwardEndIndex);
        }
        //回收后半段
        if (needRecyclerBackward) {
            recycleChildren(recycler, backwardStartIndex, backwardEndIndex);
        }
    }

    /**
     * 获取空缺Item的个数
     */

    private int getItemLength() {
        return itemOffset * getItemCount();
    }


    private int getVacantCount() {
        //item总长度
        int itemLength = getItemLength();

        //path的长度
        int pathLength = keyframes.getPathLength();

        //第一个item较Path终点的偏移量，这个偏移量是以Path的终点为起点的，
        //例如 现在一共有10个item：
        // 0___1___2___3___4___5 现在的偏移量是>0的，直到：
        // 5___6___7___8___9___0 时为0，这个时候继续向右边滚动的话，就会变成负数了
        int firstItemScrollOffset = (int) (getScrollOffset() + pathLength);

        //同上，区别就是上面的是第一个item，这个是最后一个item，
        //例如 现在一共有10个item：
        // 0___1___2___3___4___5 现在的偏移量是<0的，一直到：
        // 4___5___6___7___8___9 时为0
        //这样做就是为了：当最后一个item离开它应在的位置时(常规的滑动模式最后一个item是坐死在最后的位置的)，
        //能够及时知道，并开始计算出它下一个item索引来补上它的空位
        int lastItemScrollOffset = firstItemScrollOffset - itemLength;
        //item的总长度 + path的总长度
        int lengthOffset = itemLength + pathLength;

        //当最后一个item滑出屏幕时(根据上面的例子来讲，是向左边滑)：
        // 9_|_0___1___2___3___4
        // 开始计算的偏移量（正数），因为如果超出了屏幕而不作处理的话，
        // 下面计算空缺距离的时候，最大值只能是itemLength
        int lastItemOverflowOffset = firstItemScrollOffset > lengthOffset ?
                firstItemScrollOffset - lengthOffset : 0;

        //空缺的距离
        int vacantDistance = lastItemScrollOffset % itemLength + lastItemOverflowOffset;

        //空缺的距离 / item之间的距离 = 需补上的item个数
        return vacantDistance / itemOffset;
    }
    /**
     * 初始化需要布局的Item数据 （无限滚动模式）
     *
     * @param result    结果
     * @param itemCount Item总数
     */
    private void initNeedLayoutLoopScrollItems(List<PosTan> result, int itemCount) {
        int vacantCount = getVacantCount();
        //得出第一个可见的item索引
        mFirstVisibleItemPos = vacantCount - mItemCountInScreen - 1;
        float currentDistance;
        float fraction;
        PosTan posTan;
        int pos;
        for (int i = mFirstVisibleItemPos; i < vacantCount; i++) {
            //防止溢出
            pos = i % itemCount;
            if (pos < 0) {
                //比如现在一个有10个item，当前pos=-10，那就表示它对应的索引是0了
                if (pos == -itemCount) {
                    pos = 0;
                } else {
                    //将负数转成有效的索引
                    // [0,1,2,3,4,5,6,7,8,9]
                    // -9 --> 1   -8 --> 2
                    pos += itemCount;
                }
            }
            //得出当前距离
            currentDistance = (i + itemCount) * itemOffset - getScrollOffset();
            fraction = currentDistance / keyframes.getPathLength();
            //拿到坐标数据
            posTan = keyframes.getValue(fraction);
            if (posTan == null) {
                continue;
            }
            Log.e("dddddddddddddddddd", String.valueOf(pos));
            posTan.setIndex(pos);
            result.add(posTan);
        }
    }


    private ArrayList<PosTan> getNeedLayoutItems() {
     //   checkKeyframes();
        ArrayList<PosTan> result = new ArrayList<>(); // 可优化此处
        //item个数
        int itemCount = getItemCount();
        //满足无限滚动
        Log.e("isSatisfiedLoopScroll", String.valueOf(isSatisfiedLoopScroll()));
        Log.e("isSatisfiedLoopScroll", "d");

        if (isSatisfiedLoopScroll()) {
            initNeedLayoutLoopScrollItems(result, itemCount);
            //initNeedLayoutItems(result, itemCount);
        } else {
            initNeedLayoutItems(result, itemCount);
        }
        Log.e("mmmmmmmmmm", String.valueOf(result.size()));
        return result;
    }

    /**
     * 将基于总长度的百分比转换成基于某个片段的百分比 (解两点式直线方程)
     *
     * @param startX   片段起始百分比
     * @param endX     片段结束百分比
     * @param currentX 总长度百分比
     * @return 该片段的百分比
     */

    // 缩放算法要动态替换，使用策略模式
    private static float solveTwoPointForm(float startX, float endX, float currentX) {

        return (currentX - startX) / (endX - startX);
    }

    /**
     * 根据Item在Path上的位置来获取对应的缩放比例
     *
     * @param fraction Item位置相对于Path总长度的百分比
     * @return 该Item的缩放比例
     */
    /**
     * 根据Item在Path上的位置来获取对应的缩放比例
     *
     * @param fraction Item位置相对于Path总长度的百分比
     * @return 该Item的缩放比例
     */
    private float getScale(float fraction) {
        boolean isHasMin = false;
        boolean isHasMax = false;
        float minScale = 0;
        float maxScale = 0;
        float scalePosition;
        float minFraction = 1, maxFraction = 1;
        //必须从小到大遍历，才能找到最贴近fraction的scale
        for (int i = 1; i < mScaleRatio.length; i += 2) {
            scalePosition = mScaleRatio[i];
            //找更小的
            if (scalePosition <= fraction) {
                //得到缩放比例
                minScale = mScaleRatio[i - 1];
                //得到缩放位置
                minFraction = mScaleRatio[i];
                //标记已找到
                isHasMin = true;
            } else {
                break;
            }
        }
        //必须从大到小遍历，才能找到最贴近fraction的scale
        for (int i = mScaleRatio.length - 1; i >= 1; i -= 2) {
            scalePosition = mScaleRatio[i];
            //找更大的
            if (scalePosition >= fraction) {
                maxScale = mScaleRatio[i - 1];
                maxFraction = mScaleRatio[i];
                isHasMax = true;
            } else {
                break;
            }
        }
        //没找到对应的缩放比例，就不缩放
        if (!isHasMin) {
            minScale = 1;
        }
        if (!isHasMax) {
            maxScale = 1;
        }
        //得到相对百分比
        fraction = solveTwoPointForm(minFraction, maxFraction, fraction);
        //得到相差的比例
        float distance = maxScale - minScale;
        //得到相对的缩放比例
        float scale = distance * fraction;
        //还需在原来的基础上增加，得到绝对缩放比例
        float result = minScale + scale;
        //判断数值是否合法，如不合法，直接使用基础缩放比例
        //Log.e("dzcSa", String.valueOf(result));
        return isFinite(result) ? result : minScale;
    }

    /**
     * 判断数值是否合法
     *
     * @param value 要判断的数值
     * @return 合法为true，反之
     */
    private boolean isFinite(float value) {
        return !Float.isNaN(value) && !Float.isInfinite(value);
    }

}
