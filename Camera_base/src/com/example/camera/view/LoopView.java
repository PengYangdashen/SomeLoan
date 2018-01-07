package com.example.camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import util.LogUtil;

import com.example.camera.R;

/**
 * Created by Weidongjian on 2015/8/18.
 */
public class LoopView extends View {

	private String TAG = getClass().getSimpleName();
	
    public enum ACTION {
        // 点击，滑�?滑到尽头)，拖拽事�?
        CLICK, FLING, DAGGLE
    }
    Context context;

    Handler handler;
    private GestureDetector gestureDetector;
    OnItemSelectedListener onItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText;
    Paint paintCenterText;
    Paint paintIndicator;

    List<String> items;

    int velocityFling = 20;
    
    int textSize;
    int maxTextWidth;
    int maxTextHeight;

    int colorLightGray;
    int colorSelectorItem;
    int colorUnSelectorItem;
    int colorTransparent;

    // 条目间距倍数
    float lineSpacingMultiplier;
    boolean isLoop;

    // 第一条线Y坐标�?
    int firstLineY;
    int secondLineY;

    int totalScrollY;
    int initPosition;
    private int selectedItem;
    int preCurrentIndex;
    int change;

    // 显示几个条目
    int itemsVisible;

    int measuredHeight;
    int measuredWidth;
    int paddingLeft = 0;
    int paddingRight = 0;

    // 半圆周长
    int halfCircumference;
    // 半径
    int radius;

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;

    public LoopView(Context context) {
    	this(context, null);
    }

    public LoopView(Context context, AttributeSet attributeset) {
    	this(context, attributeset, 0);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        LogUtil.d(TAG, "进入构造方法LoopView(Context context, AttributeSet attributeset, int defStyleAttr)");
        initLoopView(context,attributeset);
    }

    private void initLoopView(Context context2, AttributeSet attributeset) {
    	this.context = context2;
    	handler = new MessageHandler(this);
    	gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
    	gestureDetector.setIsLongpressEnabled(false);
    	
    	//获取自定义属性。
        TypedArray ta = context.obtainStyledAttributes(attributeset, R.styleable.LoopView);
        lineSpacingMultiplier = ta.getFloat(R.styleable.LoopView_lineSpacingMultiplier, 2.0F);
        isLoop = ta.getBoolean(R.styleable.LoopView_isLoop, true);
        itemsVisible = ta.getInt(R.styleable.LoopView_itemsVisible, 5);
        colorSelectorItem = ta.getColor(R.styleable.LoopView_selectorItemColor, 0xffddd200);
        colorUnSelectorItem = ta.getColor(R.styleable.LoopView_unSelectorItemColor, 0xffafafaf);
        velocityFling = ta.getInt(R.styleable.LoopView_velocityFling, 20);
        
        totalScrollY = 0;
        initPosition = -1;
        
        initPaints();
        
        setTextSize(16F);
        LogUtil.d(TAG, "");
	}

	private void initLoopView(Context context) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);

        lineSpacingMultiplier = 2.0F;
        isLoop = true;
        itemsVisible = 5;
        textSize = 0;
        colorSelectorItem = 0xffddd200;
        colorUnSelectorItem = 0xffeeeeee;
        colorLightGray = 0xffc5c5c5;
        colorTransparent = 0x00c5c5c5; 

        totalScrollY = 0;
        initPosition = -1;

        initPaints();

        setTextSize(16F);
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(colorUnSelectorItem);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(colorSelectorItem);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.05F);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(colorTransparent);
        paintIndicator.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void remeasure() {
        if (items == null) {
            return;
        }

        measureTextWidthHeight();

        halfCircumference = (int) (maxTextHeight * lineSpacingMultiplier * (itemsVisible - 1));
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        radius = (int) (halfCircumference / Math.PI);
        int extraRightWidth = (int) (maxTextWidth * 0.05) + 1;
        if (paddingRight<=extraRightWidth) {
            paddingRight = extraRightWidth;
        }
        measuredWidth = maxTextWidth + paddingLeft + paddingRight;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < items.size(); i++) {
            String s1 = items.get(i);
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期
            int textHeight = rect.height();
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight;
            }
        }

    }

//    private void smoothScroll() {
//        int offset = (int) ((float) totalScrollY % (lineSpacingMultiplier * (float) maxTextHeight));
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new SmoothScrollTimerTask(this, offset, timer), 0L, 10L);
//    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action==ACTION.FLING||action==ACTION.DAGGLE) {
            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY%itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

//    void smoothScroll() {
//        int offset = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
//        cancelFuture();
//        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, offset), 0, 10, TimeUnit.MILLISECONDS);
//    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        // 修改这个值可以改变滑行�?�?
 
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture!=null&&!mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public final void setNotLoop() {
        isLoop = false;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<String> items) {
        this.items = items;
        remeasure();
        invalidate();
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingRight = right;
    }

    public final int getSelectedItem() {
        return selectedItem;
    }
//
//    protected final void scrollBy(float velocityY) {
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
//    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }

        String as[] = new String[itemsVisible];
        change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
        preCurrentIndex = initPosition + change % items.size();
//        Log.i("test", (new StringBuilder("scrollY1=")).append(totalScrollY).toString());
//        Log.i("test", (new StringBuilder("change=")).append(change).toString());
//        Log.i("test", (new StringBuilder("lineSpacingMultiplier * maxTextHeight=")).append(lineSpacingMultiplier * maxTextHeight).toString());
//        Log.i("test", (new StringBuilder("preCurrentIndex=")).append(preCurrentIndex).toString());
        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        // 设置as数组中每个元素的�?
        int k1 = 0;
        while (k1 < itemsVisible) {
            int l1 = preCurrentIndex - (itemsVisible / 2 - k1);
            if (isLoop) {
                if (l1 < 0) {
                    l1 = l1 + items.size();
                }
                if (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                as[k1] = items.get(l1);
            } else if (l1 < 0) {
                as[k1] = "";
            } else if (l1 > items.size() - 1) {
                as[k1] = "";
            } else {
                as[k1] = items.get(l1);
            }
            k1++;
        }
        int m1 = paddingLeft;
        canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);
        int j1 = 0;
        while (j1 < itemsVisible) {
            canvas.save();
            // L(弧长)=α（弧度）* r(半径) （弧度制�?
            // 求弧�?-> (L * π ) / (π * r)   (弧长X�?半圆周长)
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * j1 - j2) * Math.PI) / halfCircumference;
            // 弧度转换成角�?把半圆以Y轴为轴心向右�?0度，使其处于第一象限及第四象�?
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.drawText(as[j1], m1, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(as[j1], m1, maxTextHeight, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintCenterText);
                    selectedItem = items.indexOf(as[j1]);
                } else {
                    // 其他条目
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintOuterText);
                }
                canvas.restore();
            }
            j1++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();

                totalScrollY = (int) (totalScrollY + dy);

                // 边界处理�?
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

	public void setVelocityFling(int velocityFling) {
		this.velocityFling = velocityFling;
	}

	public void setColorSelectorItem(int colorSelectorItem) {
		this.colorSelectorItem = colorSelectorItem;
	}

	public void setColorUnSelectorItem(int colorUnSelectorItem) {
		this.colorUnSelectorItem = colorUnSelectorItem;
	}

	public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
		this.lineSpacingMultiplier = lineSpacingMultiplier;
	}

	public void setLoop(boolean isLoop) {
		this.isLoop = isLoop;
	}

	public void setItemsVisible(int itemsVisible) {
		this.itemsVisible = itemsVisible;
	}
    
}
