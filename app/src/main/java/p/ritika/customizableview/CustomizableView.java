package p.ritika.customizableview;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class CustomizableView extends FrameLayout {

    public static final String TAG = "p.ritika.customizableview";
    private BorderView ivBorder;
    private ImageView ivScale, ivRotate, ivCustom,ivDelete,ivDrag;
    private float scale_orgX = -1, scale_orgY = -1;
    private float move_orgX = -1, move_orgY = -1;
    private double centerX, centerY;
    private int CORNER_BUTTON_SIZE_DP = 20;
    private static float SELF_WIDTH_DP = 100;
    private static float SELF_HEIGHT_DP = 100;

    public CustomizableView(Context context, View mainView) {
        super(context);
        init(context, mainView);
    }

    public CustomizableView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomizableView(Context context, float viewWidthDP, float viewHeightDP, int cornerButtonSizeDP, View mainView) {
        super(context);
        SELF_HEIGHT_DP = viewHeightDP;
        SELF_WIDTH_DP = viewWidthDP;
        CORNER_BUTTON_SIZE_DP = cornerButtonSizeDP;
        init(context, mainView);
    }

    public CustomizableView(Context context, float viewWidthDP, float viewHeightDP, int cornerButtonSizeDP) {
        super(context);
        SELF_HEIGHT_DP = viewHeightDP;
        SELF_WIDTH_DP = viewWidthDP;
        CORNER_BUTTON_SIZE_DP = cornerButtonSizeDP;
        init(context, null);
    }

    public CustomizableView(Context context, AttributeSet attrs, View mainView) {
        super(context, attrs);
        init(context, mainView);
    }

    public CustomizableView(Context context, AttributeSet attrs, int defStyle, View mainView) {
        super(context, attrs, defStyle);
        init(context, mainView);
    }

    public CustomizableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public CustomizableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, null);
    }

    private void init(Context context, View mainView) {
        this.ivBorder = new BorderView(context);
        this.ivScale = new ImageView(context);
        this.ivRotate = new ImageView(context);
        this.ivDelete = new ImageView(context);
        this.ivDrag = new ImageView(context);
        this.ivCustom = new ImageView(context);

        this.ivRotate.setImageResource(R.drawable.rotate);
        this.ivScale.setImageResource(R.drawable.zoominout);
        this.ivCustom.setImageResource(R.drawable.custom);
        this.ivDelete.setImageResource(R.drawable.remove);
        this.ivDrag.setImageResource(R.drawable.drag);

        this.setTag("DraggableViewGroup");
        this.ivBorder.setTag("ivBorder");
        this.ivBorder.setElevation(10f);
        this.ivRotate.setTag("ivRotate");
        this.ivRotate.setElevation(10f);
        this.ivScale.setTag("ivScale");
        this.ivScale.setElevation(10f);
        this.ivDelete.setTag("ivDelete");
        this.ivDelete.setElevation(10f);
        this.ivDrag.setTag("DraggableViewGroup");
        this.ivDrag.setElevation(10f);
        this.ivCustom.setTag("Custom");
        this.ivCustom.setElevation(10f);
        int margin = convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()) / 2;
        int height = convertDpToPixel(SELF_HEIGHT_DP, getContext());
        int width = convertDpToPixel(SELF_WIDTH_DP, getContext());

        LayoutParams this_params =
                new LayoutParams(width, height);
        this_params.gravity = Gravity.CENTER;

        LayoutParams ivMain_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        ivMain_params.setMargins(margin, margin, margin, margin);

        LayoutParams ivBorder_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        ivBorder_params.setMargins(margin, margin, margin, margin);

        LayoutParams ivScale_params =
                new LayoutParams(
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext())
                );
        ivScale_params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        LayoutParams ivCustom_params =
                new LayoutParams(
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext())
                );
        ivCustom_params.gravity = Gravity.TOP | Gravity.LEFT;
        LayoutParams ivRotate_params =
                new LayoutParams(
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext())
                );
        ivRotate_params.gravity = Gravity.BOTTOM | Gravity.LEFT;

        LayoutParams ivDelete_params =
                new LayoutParams(
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext())
                );
        ivDelete_params.gravity = Gravity.TOP | Gravity.RIGHT;

        LayoutParams ivDrag_params =
                new LayoutParams(
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(CORNER_BUTTON_SIZE_DP, getContext())
                );
        ivDrag_params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        this.setLayoutParams(this_params);
        if (mainView != null) this.addView(mainView, ivMain_params);
        this.addView(ivBorder, ivBorder_params);
        this.addView(ivScale, ivScale_params);
        this.addView(ivRotate, ivRotate_params);
        this.addView(ivDelete, ivDelete_params);
        this.addView(ivDrag, ivDrag_params);
        this.addView(ivCustom, ivCustom_params);
        this.ivDrag.setOnTouchListener(mTouchListener);
        this.ivRotate.setOnTouchListener(mTouchListener);
        this.ivScale.setOnTouchListener(mTouchListener);
        this.ivDelete.setOnClickListener(view -> {
            if (CustomizableView.this.getParent() != null) {
                ViewGroup myCanvas = ((ViewGroup) CustomizableView.this.getParent());
                myCanvas.removeView(CustomizableView.this);
            }
        });
    }

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view.getTag().equals("DraggableViewGroup")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "customised view action down");
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "customised view action move");
                        float offsetX = event.getRawX() - move_orgX;
                        float offsetY = event.getRawY() - move_orgY;
                        onPositionChange(CustomizableView.this.getX() + offsetX, CustomizableView.this.getY() + offsetY);
                        CustomizableView.this.setX(CustomizableView.this.getX() + offsetX);
                        CustomizableView.this.setY(CustomizableView.this.getY() + offsetY);
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "customised view action up");
                        break;
                    default:
                        break;

                }
            } else if (view.getTag().equals("ivScale") || view.getTag().equals("ivRotate")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "ivScale action down");

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();
                        centerX = CustomizableView.this.getX() +
                                ((View) CustomizableView.this.getParent()).getX() +
                                (float) CustomizableView.this.getWidth() / 2;
                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        double statusBarHeight = result;
                        centerY = CustomizableView.this.getY() +
                                ((View) CustomizableView.this.getParent()).getY() +
                                statusBarHeight +
                                (float) CustomizableView.this.getHeight() / 2;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "ivScale action move");

                        double angle_diff = Math.abs(
                                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                                        - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

                        Log.v(TAG, "angle_diff: " + angle_diff);

                        double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
                        double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

                        if (view.getTag().equals("ivScale")) {
                            int width = convertDpToPixel(SELF_WIDTH_DP, getContext());
                            int height = convertDpToPixel(SELF_HEIGHT_DP, getContext());
                            if (length2 > length1 && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)) {
                                //scale up
                                double offsetX = Math.abs(event.getRawX() - scale_orgX);
                                double offsetY = Math.abs(event.getRawY() - scale_orgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                CustomizableView.this.getLayoutParams().width += offset;
                                CustomizableView.this.getLayoutParams().height += offset;
                                onScaling(true);
                            } else if (length2 < length1
                                    && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                    && CustomizableView.this.getLayoutParams().width > width / 2
                                    && CustomizableView.this.getLayoutParams().height > height / 2) {
                                //scale down
                                double offsetX = Math.abs(event.getRawX() - scale_orgX);
                                double offsetY = Math.abs(event.getRawY() - scale_orgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                CustomizableView.this.getLayoutParams().width -= offset;
                                CustomizableView.this.getLayoutParams().height -= offset;
                                onScaling(false);
                            }
                        } else {
                            double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                            Log.v(TAG, "log angle: " + angle);
                            setRotation((float) angle + 215);
                            onRotating();
                        }
                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();

                        postInvalidate();
                        requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "ivScale action up");
                        break;
                }
            }
            return true;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    public void setAllControlsHidden(boolean isHidden) {
        if (isHidden) {
            ivBorder.setVisibility(View.INVISIBLE);
            ivScale.setVisibility(View.INVISIBLE);
            ivDelete.setVisibility(View.INVISIBLE);
            ivRotate.setVisibility(View.INVISIBLE);
            ivCustom.setVisibility(View.INVISIBLE);
            ivDrag.setVisibility(View.INVISIBLE);
        } else {
            ivBorder.setVisibility(View.VISIBLE);
            ivScale.setVisibility(View.VISIBLE);
            ivCustom.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            ivRotate.setVisibility(View.VISIBLE);
            ivDrag.setVisibility(View.VISIBLE);
        }
    }

    public void setScaleIcon(int resId) {
        this.ivScale.setImageResource(resId);
    }

    public void setRotateIcon(int resId) {
        this.ivRotate.setImageResource(resId);
    }

    public void setDragIcon(int resId) {
        this.ivDrag.setImageResource(resId);
    }

    public void setDeleteIcon(int resId) {
        this.ivDelete.setImageResource(resId);
    }

    public void setBorderLineHidden(boolean isHidden) {
        if (isHidden) {
            ivBorder.setVisibility(View.INVISIBLE);
        } else {
            ivBorder.setVisibility(View.VISIBLE);
        }
    }

    public ImageView getCustomControl() {
        return ivCustom;
    }

    public void setCustomControlHidden(boolean isHidden) {
        if (isHidden) {
            ivCustom.setVisibility(View.INVISIBLE);
        } else {
            ivCustom.setVisibility(View.VISIBLE);
        }
    }

    public void setScaleControlHidden(boolean isHidden) {
        if (isHidden) {
            ivScale.setVisibility(View.INVISIBLE);
        } else {
            ivScale.setVisibility(View.VISIBLE);
        }
    }

    public void setRotateControlHidden(boolean isHidden) {
        if (isHidden) {
            ivRotate.setVisibility(View.INVISIBLE);
        } else {
            ivRotate.setVisibility(View.VISIBLE);
        }
    }

    public void setDragControlHidden(boolean isHidden) {
        if (isHidden) {
            ivDrag.setVisibility(View.INVISIBLE);
        } else {
            ivDrag.setVisibility(View.VISIBLE);
        }
    }

    public void setDeleteControlHidden(boolean isHidden) {
        if (isHidden) {
            ivDelete.setVisibility(View.INVISIBLE);
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }

    public void onScaling(boolean scaleUp) {
    }

    public void onRotating() {
    }

    private double x, y;

    private void onPositionChange(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double viewPositionX() {
        return x;
    }

    public double viewPositionY() {
        return y;
    }

    private class BorderView extends View {

        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BorderView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw customised border

            LayoutParams params = (LayoutParams) this.getLayoutParams();

            Log.v(TAG, "params.leftMargin: " + params.leftMargin);

            Rect border = new Rect();
            border.left = this.getLeft() - params.leftMargin;
            border.top = this.getTop() - params.topMargin;
            border.right = this.getRight() - params.rightMargin;
            border.bottom = this.getBottom() - params.bottomMargin;
            Paint borderPaint = new Paint();
            borderPaint.setStrokeWidth(6);
            borderPaint.setColor(Color.WHITE);
            borderPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(border, borderPaint);

        }
    }

    @Override
    public void addView(View child) {
        if (getChildCount() >= 7) {
            throw new IllegalStateException("CustomizableView can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() >= 7) {
            throw new IllegalStateException("CustomizableView can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 7) {
            throw new IllegalStateException("CustomizableView can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 7) {
            throw new IllegalStateException("CustomizableView can host only one direct child");
        }

        super.addView(child, index, params);
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
}