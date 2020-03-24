package com.example.blu_main_test1.product_viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.example.blu_main_test1.R;

/**
 * 모든 운영 체제 버전의 해상도 조정을 고려하십시오
 * Created by xmuSistone on 2016/9/18.
 */
public class DragLayout extends FrameLayout {

    private int bottomDragVisibleHeight; // 보이는 높이
    private int bototmExtraIndicatorHeight; // 바닥 높이 표시기
    private int dragTopDest = 0; // 윗면 슬라이드 대상 위치
    private static final int DECELERATE_THRESHOLD = 120;
    private static final int DRAG_SWITCH_DISTANCE_THRESHOLD = 100;
    private static final int DRAG_SWITCH_VEL_THRESHOLD = 800;

    private static final float MIN_SCALE_RATIO = 0.5f;
    private static final float MAX_SCALE_RATIO = 1.0f;

    private static final int STATE_CLOSE = 1;
    private static final int STATE_EXPANDED = 2;
    private int downState; // 누르면 상태

    private final ViewDragHelper mDragHelper;
    private final GestureDetectorCompat moveDetector;
    private int mTouchSlop = 5; // 슬라이딩으로 판단되는 임계 값, 단위는 픽셀
    private int originX, originY; // 초기 상태에서 topView의 좌표
    private View bottomView, topView; // FrameLayout의 두 자식 뷰

    private GotoDetailListener gotoDetailListener;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.app, 0, 0);
        bottomDragVisibleHeight = (int) a.getDimension(R.styleable.app_bottomDragVisibleHeight, 0);
        bototmExtraIndicatorHeight = (int) a.getDimension(R.styleable.app_bototmExtraIndicatorHeight, 0);
        a.recycle();

        mDragHelper = ViewDragHelper
                .create(this, 10f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
        moveDetector = new GestureDetectorCompat(context, new MoveDetector());
        moveDetector.setIsLongpressEnabled(false); // 긴 프레스 이벤트를 처리하지 마십시오

        // 슬라이딩 거리 임계 값은 시스템에서 제공합니다
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bottomView = getChildAt(0);
        topView = getChildAt(1);

        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 콜백 클릭
                int state = getCurrentState();
                if (state == STATE_CLOSE) {
                    // 클릭시 초기 상태, 확장해야 함
                    if (mDragHelper.smoothSlideViewTo(topView, originX, dragTopDest)) {  //child주어진 (왼쪽, 위쪽) 위치로 뷰 를 애니메이션합니다 .
                        ViewCompat.postInvalidateOnAnimation(DragLayout.this);  //다음 애니메이션 시간 단계, 일반적으로 다음 디스플레이 프레임에서 무효화가 발생합니다.
                    }
                } else {
                    // 클릭하면 확장되어 세부 정보 페이지로 바로 들어갑니다.
                   gotoDetailActivity();
                }
            }
        });
    }

    // 다음 페이지로 이동
    private void gotoDetailActivity() {
        if (null != gotoDetailListener) {
            gotoDetailListener.gotoDetail();
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == topView) {
                processLinkageView();
            }
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == topView) {
                return true;
            }
            return false;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int currentTop = child.getTop();
            if (top > child.getTop()) {
                // 아래로 드래그하면 저항이 최소화됩니다
                return currentTop + (top - currentTop) / 2;
            }

            int result;
            if (currentTop > DECELERATE_THRESHOLD * 3) {
                result = currentTop + (top - currentTop) / 2;
            } else if (currentTop > DECELERATE_THRESHOLD * 2) {
                result = currentTop + (top - currentTop) / 4;
            } else if (currentTop > 0) {
                result = currentTop + (top - currentTop) / 8;
            } else if (currentTop > -DECELERATE_THRESHOLD) {
                result = currentTop + (top - currentTop) / 16;
            } else if (currentTop > -DECELERATE_THRESHOLD * 2) {
                result = currentTop + (top - currentTop) / 32;
            } else if (currentTop > -DECELERATE_THRESHOLD * 3) {
                result = currentTop + (top - currentTop) / 48;
            } else {
                result = currentTop + (top - currentTop) / 64;
            }
            return result;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 600;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 600;
        }

        @Override
        //자식 뷰가 더 이상 드래그되지 않을 때 호출됩니다.
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalY = originY;
            if (downState == STATE_CLOSE) {
                // 누르면 상태는 초기 상태입니다.
                if (originY - releasedChild.getTop() > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel < -DRAG_SWITCH_VEL_THRESHOLD) {
                    finalY = dragTopDest;
                }
            } else {
                // 누르면 상태는 확장 된 상태입니다.
                boolean gotoBottom = releasedChild.getTop() - dragTopDest > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel > DRAG_SWITCH_VEL_THRESHOLD;
                if (!gotoBottom) {
                    finalY = dragTopDest;

                    // 눌렀을 때 이미 펼쳐져 있으면 다시 위로 끌면 세부 정보 페이지로 들어갑니다.
                    if (dragTopDest - releasedChild.getTop() > mTouchSlop) {
                         gotoDetailActivity();
                        postResetPosition();
                        return;
                    }
                }
            }

            if (mDragHelper.smoothSlideViewTo(releasedChild, originX, finalY)) {
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
        }
    }


    private void postResetPosition() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                topView.offsetTopAndBottom(dragTopDest - topView.getTop());
            }
        }, 500);
    }

    /**
     * 상단 ImageView의 위치가 변경되고 하단 뷰의 배율을 조정해야합니다.
     */
    private void processLinkageView() {
        if (topView.getTop() > originY) {
            bottomView.setAlpha(0);
        } else {
            float alpha = (originY - topView.getTop()) * 0.01f;
            if (alpha > 1) {
                alpha = 1;
            }
            bottomView.setAlpha(alpha);
            int maxDistance = originY - dragTopDest;
            int currentDistance = topView.getTop() - dragTopDest;
            float scaleRatio = 1;
            float distanceRatio = (float) currentDistance / maxDistance;
            if (currentDistance > 0) {
                scaleRatio = MIN_SCALE_RATIO + (MAX_SCALE_RATIO - MIN_SCALE_RATIO) * (1 - distanceRatio);
            }
            bottomView.setScaleX(scaleRatio);
            bottomView.setScaleY(scaleRatio);
        }
    }

    class MoveDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {
            // 드래그하면 터치가 내려 가지 않습니다.
            return Math.abs(dy) + Math.abs(dx) > mTouchSlop;
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 현재 상태 확인
     */
    private int getCurrentState() {
        int state;
        if (Math.abs(topView.getTop() - dragTopDest) <= mTouchSlop) {
            state = STATE_EXPANDED;
        } else {
            state = STATE_CLOSE;
        }
        return state;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }

        super.onLayout(changed, left, top, right, bottom);

        originX = (int) topView.getX();
        originY = (int) topView.getY();
        dragTopDest = bottomView.getBottom() - bottomDragVisibleHeight - topView.getMeasuredHeight();
    }

    /* 터치 이벤트의 인터셉트 및 처리는 mDraghelper로 전달됩니다. */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 1. 탐지기와 mDragHelper는 차단이 필요한지 결정
        boolean yScroll = moveDetector.onTouchEvent(ev);
        boolean shouldIntercept = false;
        try {
            shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
        }

        // 2. 접점이 mDragHelper에 직접 눌러 질 때
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            downState = getCurrentState();
            mDragHelper.processTouchEvent(ev);
        }

        return shouldIntercept && yScroll;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // bottomMarginTop 높이 계산，여전히 명확한 수학적 모델이 필요합니다。
        // 달성 된 효과，TopView.top 및 bottomView.bottom은 확장 전후에 중앙에 있습니다.
        int bottomMarginTop = (bottomDragVisibleHeight + topView.getMeasuredHeight() / 2 - bottomView.getMeasuredHeight() / 2) / 2 - bototmExtraIndicatorHeight;
        FrameLayout.LayoutParams lp1 = (LayoutParams) bottomView.getLayoutParams();
        lp1.setMargins(0, bottomMarginTop, 0, 0);
        bottomView.setLayoutParams(lp1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //mDragHelper에 균일하게 전달되면 DragHelperCallback에 의해 드래그 효과가 실현됩니다.
        try {
            mDragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void setGotoDetailListener(GotoDetailListener gotoDetailListener) {
        this.gotoDetailListener = gotoDetailListener;
    }

    public interface GotoDetailListener {
        public void gotoDetail();
    }
}