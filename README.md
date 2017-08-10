# SplashView
 自定义view————广告弹窗

今天给大家带来自定义view————广告弹窗，效果图中的照片文字都是随便找的，实际项目中根据UI的设计稍微修改修改基本上就能达到预想的效果了(注：女神刘亦菲的图是好好找的！！！)

![效果图](https://user-gold-cdn.xitu.io/2017/8/10/edfa64620248f724e5526bb7daee61be)

效果图看完了，接下来分析一下实现过程，首先整体是继承Dialog去实现的，组合自定义view达到预想效果，倒计时是一个画圈圈的过程，接下来详细讲解吧

### 画给倒计时的圈圈

一个背景色的底盘drawCircle()，一个画圈圈drawArc(),一个文字的显示，今天这里的文字显示采取了一个新的实现方式StaticLayout(android中处理文字换行的一个工具类)

```
//画底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        //画边框
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
        } else {
            rectF = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
        }
        canvas.drawArc(rectF, -90, progress, false, borderPaint);
        //画居中的文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);

```

看到这里staticLayout是那里来的呀，看下面就知道了

```
    int textWidth = (int) textPaint.measureText(text.substring(0, text.length()));
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    
```
静态的圈圈背景都已经就位，现在开始执行倒计时动画，这里写的是5s

### 是时候让圈圈动起来了

这里采用倒计时器来倒计时然后通知刷新动画
 
```

   //倒计时
     public void start() {
         if (listener != null) {
             listener.onStartCount();
         }
         //倒计时器     第一个参数表示总时间，第二个参数表示间隔时间
         new CountDownTimer((long) lastTime, 30) {
             @Override
             public void onTick(long millisUntilFinished) {
                 progress = 360 - ((lastTime - millisUntilFinished) / lastTime) * 360;
                 invalidate();
             }
 
             @Override
             public void onFinish() {
                 progress = 0;
                 invalidate();
                 if (listener != null) {
                     listener.onFinishCount();
                 }
             }
         }.start();
     }
 
```

倒计时的准备工作做好了，将倒计时的完整代码奉上

```
/**
 * Created by Administrator on 2017/8/10.
 * 广告倒计时
 *
 * @auther madreain
 */

public class CountDownView extends View {

    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final float BORDER_WIDTH = 15f;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final String TEXT = "跳过";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    int width;
    int height;
    int min;

    private int backgroundColor;
    private float borderWidth;
    private int borderColor;
    private String text;
    private int textColor;
    private float textSize;

    private Paint circlePaint;
    private TextPaint textPaint;
    private Paint borderPaint;

    private float progress = 360;
    private StaticLayout staticLayout;

    private CountDownTimerListener listener;

    //倒计时 显示5秒什么的
    float lastTime = 5000;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        backgroundColor = ta.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        borderWidth = ta.getDimension(R.styleable.CountDownView_borders_width, BORDER_WIDTH);
        borderColor = ta.getColor(R.styleable.CountDownView_borders_color, BORDER_COLOR);
        text = ta.getString(R.styleable.CountDownView_text);
        if (text == null) {
            text = TEXT;
        }
        textSize = ta.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
        textColor = ta.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
        ta.recycle();
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(backgroundColor);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) textPaint.measureText(text.substring(0, text.length()));
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = staticLayout.getWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = staticLayout.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        min = Math.min(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        //画边框
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
        } else {
            rectF = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
        }
        canvas.drawArc(rectF, -90, progress, false, borderPaint);
        //画居中的文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
    }


    //倒计时
    public void start() {
        if (listener != null) {
            listener.onStartCount();
        }
        //倒计时器     第一个参数表示总时间，第二个参数表示间隔时间
        new CountDownTimer((long) lastTime, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = 360 - ((lastTime - millisUntilFinished) / lastTime) * 360;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress = 0;
                invalidate();
                if (listener != null) {
                    listener.onFinishCount();
                }
            }
        }.start();
    }

    public void setCountDownTimerListener(CountDownTimerListener listener) {
        this.listener = listener;
    }

    public interface CountDownTimerListener {
        void onStartCount();

        void onChangeCount(int second);

        void onFinishCount();
    }
}


```

### 广告弹窗

广告弹窗上的倒计时已经准备好了，开始组合自定义view的撰写了，后期实际项目开发中，这一块可以根据实际情况修改组合的view布局

```
/**
 * Created by Administrator on 2017/8/10.
 * 闪屏广告
 *
 * @auther madreain
 */

public class SplashDialog extends Dialog {

    private Context mContext;
    //展示的
    AdvertisingImgModel advertisingImgModel;

    //背景照片
    private ImageView img_background;
    //倒计时
    private CountDownView count_down_view;

    private OnSplashDetailClickListener onSplashDetailClickListener;

    public SplashDialog(Context context, AdvertisingImgModel advertisingImgModel) {
        super(context, R.style.ADDialog);
        mContext = context;
        this.advertisingImgModel = advertisingImgModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_dialog);
        full(true);
        initView();
    }

    /**
     * @param enable false 显示，true 隐藏
     */
    private void full(boolean enable) {
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        if (enable) {

            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;//|=：或等于，取其一

        } else {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);//&=：与等于，取其二同时满足，     ~ ： 取反

        }
        getWindow().setAttributes(p);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initView() {

        img_background = (ImageView) findViewById(R.id.img_background);
        Glide.with(mContext).load(advertisingImgModel.getImgurl()).into(img_background);
        //广告倒计时
        count_down_view = (CountDownView) findViewById(R.id.count_down_view);
        count_down_view.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {

            }

            @Override
            public void onChangeCount(int second) {

            }

            @Override
            public void onFinishCount() {
                dismiss();
            }
        });
        //启动倒计时
        count_down_view.start();
        //点击跳过按钮
        count_down_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击事件
        img_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSplashDetailClickListener != null) {
                    onSplashDetailClickListener.onSplashDetailClick(advertisingImgModel);
                }
                dismiss();
            }
        });

    }


    public void setOnSplashDetailClickListener(OnSplashDetailClickListener onSplashDetailClickListener) {
        this.onSplashDetailClickListener = onSplashDetailClickListener;
    }

    public interface OnSplashDetailClickListener {
        void onSplashDetailClick(AdvertisingImgModel advertisingImgModel);
    }

}

```

### 代码中使用

```
  txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertisingImgModel advertisingImgModel = new AdvertisingImgModel(1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/34b6d85c406894f3803d949a78c4546e.jpg");
                splashDialog = new SplashDialog(MainActivity.this, advertisingImgModel);
                splashDialog.setOnSplashDetailClickListener(new SplashDialog.OnSplashDetailClickListener() {
                    @Override
                    public void onSplashDetailClick(AdvertisingImgModel advertisingImgModel) {
                        Toast.makeText(MainActivity.this, "跳转到广告的详情页", Toast.LENGTH_SHORT).show();
                    }
                });
                splashDialog.show();
            }
        });

```

[个人博客](https://madreain.github.io/)
 
