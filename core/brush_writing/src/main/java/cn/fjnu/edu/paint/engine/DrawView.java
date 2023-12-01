package cn.fjnu.edu.paint.engine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ZoomControls;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import cn.fjnu.edu.paint.bean.CanvasInfo;
import cn.fjnu.edu.paint.bean.ColorSelectInfo;
import cn.fjnu.edu.paint.bean.DrawInfo;
import cn.fjnu.edu.paint.bean.DrawPointInfo;
import cn.fjnu.edu.paint.bean.ExtendDrawInfo;
import cn.fjnu.edu.paint.bean.LayerInfo;
import cn.fjnu.edu.paint.bean.PastePhotoSaveInfo;
import cn.fjnu.edu.paint.bean.PointInfo;
import cn.fjnu.edu.paint.data.GlobalValue;
import cn.fjnu.edu.paint.domain.BrokenPoint;
import cn.fjnu.edu.paint.service.MainCanvasOP;
import cn.fjnu.edu.paint.utils.DrawUtils;
import cn.fjnu.edu.paint.utils.PaintAppUtils;
//import cn.fjnu.edu.paint.view.AppColorSelectDialog;
//import cn.fjnu.edu.paint.view.AppCommonTipDialog;
//import cn.fjnu.edu.paint.view.AreaSelectDialog;
//import cn.fjnu.edu.ui.activity.PaintMainActivity;
//import cn.fjnu.edu.ui.activity.PaperPaintMainActivity2;
//import cn.fjnu.edu.ui.activity.PaperVIPActivity;
//import cn.fjnu.edu.ui.activity.VIPActivity;
//import cn.flynormal.baselib.bean.UserInfo;
//import cn.flynormal.baselib.data.BaseGlobalValue;
//import cn.flynormal.baselib.service.HuaweiStorageManagerService;
//import cn.flynormal.baselib.service.SharedPreferenceService;
//import cn.flynormal.baselib.utils.ActivityUtils;
//import cn.flynormal.baselib.utils.AppUtils;
//import cn.flynormal.baselib.utils.BaseAppUtils;
//import cn.flynormal.baselib.utils.BitmapUtils;
//import cn.flynormal.baselib.utils.DeviceUtils;
//import cn.flynormal.baselib.utils.DialogUtils;
//import cn.flynormal.baselib.utils.Md5Utils;
//import cn.flynormal.baselib.utils.PixeUtils;
//import cn.flynormal.baselib.utils.ViewUtils;
import com.google.gson.Gson;
//import io.reactivex.Flowable;
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@SuppressLint({"ClickableViewAccessibility"})
/* loaded from: classes.dex */
public class DrawView extends ImageView {
    public static boolean A0;
    private SeekBar seekBar;
    public int B;
    public int C;
    public float D;
    private int paintTextColor;
    private int F;
    private Paint paint;
    private final Context context;
    private int M;
    private boolean N;
    private AppColorSelectDialog colorSelectDialog;
    private List<PointInfo> P;
    private List<PointInfo> Q;
    private List<DrawPointInfo> R;
    private List<DrawPointInfo> S;
    private int T;
    private ColorSelectInfo colorSelectInfo;
    private AreaSelectDialog areaSelectDialog;
    private AppCommonTipDialog commonTipDialog;

    /* renamed from: a */
    private int f268a;
    private OnUserTouchListener onUserTouchListener;

    /* renamed from: b */
    private int mPaintMode;
    private AlertDialog b0;

    /* renamed from: c */
    private int mLastMode;
    private Disposable c0;

    /* renamed from: d */
    private int f271d;
    private LayerInfo d0;

    /* renamed from: e */
    private int mShapeValue;
    private List<LayerInfo> e0;

    /* renamed from: f */
    private float mPenSize;
    private Canvas mCanvas;
    private BrokenPoint g;
    private final Object mLockObject;

    /* renamed from: h */
    private float f274h;
    private Path path;

    /* renamed from: i */
    private float f275i;
    private Paint i0;
    private Matrix j;
    private List<Float> j0;

    /* renamed from: k */
    private Bitmap bitmap;
    private int k0;

    /* renamed from: l */
    private boolean f277l;
    private int l0;
    private boolean m;
    private float m0;
    private RectF mRectF;
    private float n0;
    private List<Path> o;
    private float o0;
    private List<Path> pathList;
    private float p0;
    public Paint layerPaint;
    private boolean q0;
    public List<Paint> paintList;
    private Bitmap r0;
    public List<Paint> s;
    private Bitmap s0;
    private Path t;
    private DrawInfo t0;
    private Paint u;
    private DrawInfo u0;
    private float v;
    private float v0;
    private float w;
    private float w0;
    private float x;
    private boolean x0;
    private float y;
    private int y0;
    private SeekBar z;
    private float[] z0;

    /* loaded from: classes.dex */
    public interface OnDrawPastePhotoFinishedListener {
        void a();
    }

    /* loaded from: classes.dex */
    public interface OnSavePhotoListener {
        void a(boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnUserTouchListener {
        void a();

        void b();
    }

    /* loaded from: classes.dex */
    public class a implements Function<List<PastePhotoSaveInfo>, Integer> {

        /* renamed from: cn.fjnu.edu.paint.engine.DrawView$a$a */
        /* loaded from: classes.dex */
        public class RunnableC0002a implements Runnable {

            /* renamed from: a */
            final /* synthetic */ Bitmap mBitMap;

            /* renamed from: b */
            final /* synthetic */ Matrix mMatrix;

            RunnableC0002a(Bitmap bitmap, Matrix matrix) {
                a.this = r1;
                this.mBitMap = bitmap;
                this.mMatrix = matrix;
            }

            @Override // java.lang.Runnable
            public void run() {
                DrawView.this.mCanvas.drawBitmap(this.mBitMap, this.mMatrix, DrawView.this.layerPaint);
                if (!this.mBitMap.isRecycled()) {
                    this.mBitMap.recycle();
                }
                synchronized (DrawView.this.mLockObject) {
                    try {
                        DrawView.this.mLockObject.notify();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }

        a() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public Integer apply(@NonNull List<PastePhotoSaveInfo> list) throws Exception {
            for (PastePhotoSaveInfo pastePhotoSaveInfo : list) {
                Bitmap bitmap = pastePhotoSaveInfo.getBitmap();
                Matrix matrix = pastePhotoSaveInfo.getMatrix();
                if (DrawView.this.context instanceof Activity) {
                    ((Activity) DrawView.this.context).runOnUiThread(new RunnableC0002a(bitmap, matrix));
                    synchronized (DrawView.this.mLockObject) {
                        try {
                            DrawView.this.mLockObject.wait();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                DrawInfo drawInfo = new DrawInfo();
                drawInfo.setMode(6);
                drawInfo.setImgType(5);
                drawInfo.setOriginImgType(5);
                DrawView.this.T0(drawInfo);
                DrawView.this.J(drawInfo);
            }
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public class b implements Consumer<Integer> {
        b() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Integer num) throws Exception {
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class c implements Consumer<Throwable> {
        c() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Throwable th) throws Exception {
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class d implements Function<Integer, Integer> {
        d() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public Integer apply(@NonNull Integer num) throws Exception {
            DrawInfo drawInfo = new DrawInfo();
            drawInfo.setMode(5);
            drawInfo.setImgType(9);
            drawInfo.setOriginImgType(9);
            DrawView.this.T0(drawInfo);
            DrawView.this.J(drawInfo);
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public class e implements Runnable {
        e() {
            DrawView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            DrawView.this.I0();
        }
    }

    /* loaded from: classes.dex */
    public class f implements AppCommonTipDialog.OnConfirmListener {
        f() {
            DrawView.this = r1;
        }

        @Override // cn.fjnu.edu.paint.view.AppCommonTipDialog.OnConfirmListener
        public void a() {
            if (DeviceUtils.k()) {
                ActivityUtils.startActivity(DrawView.this.context, PaperVIPActivity.class);
            } else {
                ActivityUtils.startActivity(DrawView.this.context, VIPActivity.class);
            }
        }
    }

    /* loaded from: classes.dex */
    public class g implements Consumer<Integer> {
        g() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Integer num) throws Exception {
            DrawView.this.N();
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class h implements Consumer<Throwable> {
        h() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Throwable th) throws Exception {
            DrawView.this.N();
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class i implements Function<Integer, Integer> {
        i() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public Integer apply(@NonNull Integer num) throws Exception {
            DrawInfo drawInfo = new DrawInfo();
            drawInfo.setMode(2);
            drawInfo.setImgType(6);
            drawInfo.setOriginImgType(6);
            DrawView.this.T0(drawInfo);
            DrawView.this.J(drawInfo);
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public class j implements Consumer<Integer> {
        j() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Integer num) throws Exception {
            DrawView.this.invalidate();
            DrawView.this.N();
        }
    }

    /* loaded from: classes.dex */
    public class k implements Consumer<Throwable> {
        k() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public void accept(Throwable th) throws Exception {
            DrawView.this.invalidate();
            DrawView.this.N();
        }
    }

    /* loaded from: classes.dex */
    public class l implements Function<Integer, Integer> {
        l() {
            DrawView.this = r1;
        }

        /* renamed from: a */
        public Integer apply(@NonNull Integer num) throws Exception {
            DrawInfo drawInfo = new DrawInfo();
            drawInfo.setMode(4);
            drawInfo.setImgType(7);
            drawInfo.setOriginImgType(7);
            DrawView.this.T0(drawInfo);
            DrawView.this.J(drawInfo);
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public class m implements AreaSelectDialog.OnSelectListener {

        /* loaded from: classes.dex */
        class a implements SeekBar.OnSeekBarChangeListener {

            /* renamed from: a */
            private Paint f294a;

            /* renamed from: b */
            private Paint f295b;

            /* renamed from: c */
            private Bitmap f296c;

            /* renamed from: d */
            private Bitmap f297d;

            /* renamed from: e */
            private float f298e;

            /* renamed from: f */
            private float f299f;
            private float g;

            /* renamed from: h */
            private float f300h;

            /* renamed from: i */
            private float f301i;
            private float j;

            a() {
                m.this = r1;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z && i2 != 0) {
                    float f2 = ((float) i2) / 50.0f;
                    DrawView.this.j.setScale(f2, f2, this.f298e, this.f299f);
                    Bitmap bitmap = this.f296c;
                    if (bitmap != null && bitmap.getWidth() > 0 && this.f296c.getHeight() > 0) {
                        Bitmap bitmap2 = this.f296c;
                        Bitmap createBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), this.f296c.getHeight(), DrawView.this.j, true);
                        this.f297d = createBitmap;
                        if (createBitmap != null) {
                            if (this.g > 0.0f && this.f300h > 0.0f && this.f301i > 0.0f && this.j > 0.0f) {
                                DrawView.this.mCanvas.drawRect(this.g, this.f300h, this.f301i, this.j, this.f295b);
                            }
                            this.g = this.f298e - ((((float) this.f297d.getWidth()) * 1.0f) / 2.0f);
                            this.f300h = this.f299f - ((((float) this.f297d.getHeight()) * 1.0f) / 2.0f);
                            this.f301i = this.f298e + ((((float) this.f297d.getWidth()) * 1.0f) / 2.0f);
                            this.j = this.f299f + ((((float) this.f297d.getHeight()) * 1.0f) / 2.0f);
                            DrawView.this.mCanvas.drawRect(this.g, this.f300h, this.f301i, this.j, this.f295b);
                            DrawView.this.mCanvas.drawBitmap(this.f297d, this.g, this.f300h, this.f294a);
                            DrawView.this.invalidate();
                        }
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (this.f294a == null) {
                    Paint paint = new Paint();
                    this.f294a = paint;
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                }
                if (this.f295b == null) {
                    Paint paint2 = new Paint();
                    this.f295b = paint2;
                    paint2.setAlpha(0);
                    this.f295b.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                if (DrawView.this.bitmap != null && DrawView.this.bitmap.getWidth() != 0 && DrawView.this.bitmap.getHeight() != 0) {
                    DrawView.this.j = new Matrix();
                    this.f296c = Bitmap.createBitmap(DrawView.this.bitmap);
                    this.f298e = DrawView.this.v + ((((float) this.f296c.getWidth()) * 1.0f) / 2.0f);
                    this.f299f = DrawView.this.w + ((((float) this.f296c.getHeight()) * 1.0f) / 2.0f);
                    DrawView.this.mCanvas.drawRect(this.g, this.f300h, this.f301i, this.j, this.f295b);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (this.f297d != null) {
                    DrawInfo drawInfo = new DrawInfo();
                    drawInfo.setOriginImgType(4);
                    drawInfo.setImgType(4);
                    drawInfo.setMode(2);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    arrayList.add(new PointInfo(this.g, this.f300h));
                    arrayList.add(new PointInfo(this.f301i, this.j));
                    arrayList.add(new PointInfo(DrawView.this.v, DrawView.this.w));
                    arrayList.add(new PointInfo(DrawView.this.x, DrawView.this.y));
                    drawInfo.setPointInfos(arrayList);
                    arrayList2.add(new DrawPointInfo(this.g, this.f300h, 1.0f));
                    arrayList2.add(new DrawPointInfo(this.f301i, this.j, 1.0f));
                    arrayList2.add(new DrawPointInfo(DrawView.this.v, DrawView.this.w, 1.0f));
                    arrayList2.add(new DrawPointInfo(DrawView.this.x, DrawView.this.y, 1.0f));
                    drawInfo.setDrawPointInfos(arrayList2);
                    DrawView.this.T0(drawInfo);
                    DrawView.this.J(drawInfo);
                }
            }
        }

        /* loaded from: classes.dex */
        class DrawSeekBarListener implements SeekBar.OnSeekBarChangeListener {

            /* renamed from: a */
            private Paint f303a;

            /* renamed from: b */
            private Paint f304b;

            /* renamed from: c */
            private Bitmap f305c;

            /* renamed from: d */
            private Bitmap f306d;

            /* renamed from: e */
            private float f307e;

            /* renamed from: f */
            private float f308f;
            private float g;

            /* renamed from: h */
            private float f309h;

            /* renamed from: i */
            private float f310i;
            private float j;

            DrawSeekBarListener() {
                m.this = r1;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    DrawView.this.j.setRotate((float) i2, this.f307e, this.f308f);
                    Bitmap bitmap = this.f305c;
                    if (bitmap != null && bitmap.getWidth() > 0 && this.f305c.getHeight() > 0) {
                        Bitmap bitmap2 = this.f305c;
                        Bitmap createBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), this.f305c.getHeight(), DrawView.this.j, true);
                        this.f306d = createBitmap;
                        if (createBitmap != null) {
                            if (this.g > 0.0f && this.f309h > 0.0f && this.f310i > 0.0f && this.j > 0.0f) {
                                DrawView.this.mCanvas.drawRect(this.g, this.f309h, this.f310i, this.j, this.f304b);
                            }
                            this.g = this.f307e - ((((float) this.f306d.getWidth()) * 1.0f) / 2.0f);
                            this.f309h = this.f308f - ((((float) this.f306d.getHeight()) * 1.0f) / 2.0f);
                            this.f310i = this.f307e + ((((float) this.f306d.getWidth()) * 1.0f) / 2.0f);
                            this.j = this.f308f + ((((float) this.f306d.getHeight()) * 1.0f) / 2.0f);
                            DrawView.this.mCanvas.drawRect(this.g, this.f309h, this.f310i, this.j, this.f304b);
                            DrawView.this.mCanvas.drawBitmap(this.f306d, this.g, this.f309h, this.f303a);
                            DrawView.this.invalidate();
                        }
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (this.f303a == null) {
                    Paint paint = new Paint();
                    this.f303a = paint;
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                }
                if (this.f304b == null) {
                    Paint paint2 = new Paint();
                    this.f304b = paint2;
                    paint2.setAlpha(0);
                    this.f304b.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                DrawView.this.j = new Matrix();
                this.f305c = Bitmap.createBitmap(DrawView.this.bitmap);
                this.f307e = DrawView.this.v + ((((float) this.f305c.getWidth()) * 1.0f) / 2.0f);
                this.f308f = DrawView.this.w + ((((float) this.f305c.getHeight()) * 1.0f) / 2.0f);
                DrawView.this.mCanvas.drawRect(this.g, this.f309h, this.f310i, this.j, this.f304b);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (this.f306d != null) {
                    DrawInfo drawInfo = new DrawInfo();
                    drawInfo.setOriginImgType(1);
                    drawInfo.setImgType(1);
                    drawInfo.setMode(2);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new PointInfo(this.g, this.f309h));
                    arrayList.add(new PointInfo(this.f310i, this.j));
                    drawInfo.setPointInfos(arrayList);
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(new DrawPointInfo(this.g, this.f309h, 1.0f));
                    arrayList2.add(new DrawPointInfo(this.f310i, this.j, 1.0f));
                    drawInfo.setDrawPointInfos(arrayList2);
                    DrawView.this.T0(drawInfo);
                    DrawView.this.J(drawInfo);
                }
            }
        }

        /* loaded from: classes.dex */
        class c implements AppColorSelectDialog.OnColorSelectListener {
            c() {
                m.this = r1;
            }

            @Override // cn.fjnu.edu.paint.view.AppColorSelectDialog.OnColorSelectListener
            public void a(int i2, int i3, int i4, int i5, int i6, int i7) {
                DrawView.this.colorSelectInfo = new ColorSelectInfo(i2, i3, i4, i5, i6, i7);
                DrawView.this.setCutFillColor(i7);
                DrawInfo drawInfo = new DrawInfo();
                drawInfo.setMode(2);
                drawInfo.setImgType(10);
                drawInfo.setOriginImgType(10);
                drawInfo.setPaintColor(DrawView.this.f268a);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new PointInfo(DrawView.this.v, DrawView.this.w));
                arrayList.add(new PointInfo(DrawView.this.x, DrawView.this.y));
                drawInfo.setPointInfos(arrayList);
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(new DrawPointInfo(DrawView.this.v, DrawView.this.w, 1.0f));
                arrayList2.add(new DrawPointInfo(DrawView.this.x, DrawView.this.y, 1.0f));
                drawInfo.setDrawPointInfos(arrayList2);
                DrawView.this.J(drawInfo);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(DrawView.this.f268a);
                DrawView.this.mCanvas.drawRect(DrawView.this.v, DrawView.this.w, DrawView.this.x, DrawView.this.y, paint);
                DrawView.this.invalidate();
            }
        }

        m() {
            DrawView.this = r1;
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void a() {
            try {
                Bitmap topLayerBitmap = DrawView.this.getTopLayerBitmap();
                if (topLayerBitmap != null) {
                    DrawView drawView = DrawView.this;
                    drawView.bitmap = Bitmap.createBitmap(topLayerBitmap, (int) drawView.v, (int) DrawView.this.w, (int) Math.abs(DrawView.this.x - DrawView.this.v), (int) Math.abs(DrawView.this.y - DrawView.this.w));
                    DrawView.this.T = 0;
                    if (DrawView.this.bitmap == null) {
                        Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
                    }
                }
            } catch (Error unused) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            } catch (Exception unused2) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            }
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void b() {
            if (DrawView.this.colorSelectDialog == null) {
                DrawView.this.colorSelectDialog = new AppColorSelectDialog(DrawView.this.context, new c());
            }
            DrawView.this.colorSelectDialog.x(DrawView.this.colorSelectInfo);
            DrawView.this.colorSelectDialog.y(DrawView.this.context.getString(R.string.select_area_fill_color));
            if (DrawView.this.context instanceof PaintMainActivity) {
                DrawView.this.colorSelectDialog.v(PaintAppUtils.l(x.a()) ? 0 : ((PaintMainActivity) DrawView.this.context).F1());
            }
            DrawView.this.colorSelectDialog.show();
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void c() {
            Paint paint = new Paint();
            paint.setAlpha(0);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            DrawInfo drawInfo = new DrawInfo();
            drawInfo.setMode(2);
            drawInfo.setImgType(2);
            drawInfo.setOriginImgType(2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new PointInfo(DrawView.this.v, DrawView.this.w));
            arrayList.add(new PointInfo(DrawView.this.x, DrawView.this.y));
            drawInfo.setPointInfos(arrayList);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new DrawPointInfo(DrawView.this.v, DrawView.this.w, 1.0f));
            arrayList2.add(new DrawPointInfo(DrawView.this.x, DrawView.this.y, 1.0f));
            drawInfo.setDrawPointInfos(arrayList2);
            DrawView.this.J(drawInfo);
            DrawView.this.mRectF = new RectF(DrawView.this.v, DrawView.this.w, DrawView.this.x, DrawView.this.y);
            DrawView.this.mCanvas.drawRect(DrawView.this.mRectF, paint);
            DrawView.this.invalidate();
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void d() {
            try {
                Bitmap topLayerBitmap = DrawView.this.getTopLayerBitmap();
                if (topLayerBitmap != null) {
                    DrawView drawView = DrawView.this;
                    drawView.bitmap = Bitmap.createBitmap(topLayerBitmap, (int) drawView.v, (int) DrawView.this.w, (int) Math.abs(DrawView.this.x - DrawView.this.v), (int) Math.abs(DrawView.this.y - DrawView.this.w));
                    DrawView.this.T = 1;
                    if (DrawView.this.bitmap == null) {
                        Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
                        return;
                    }
                    DrawInfo drawInfo = new DrawInfo();
                    drawInfo.setMode(2);
                    drawInfo.setImgType(8);
                    drawInfo.setOriginImgType(8);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    arrayList.add(new PointInfo(DrawView.this.v, DrawView.this.w));
                    arrayList.add(new PointInfo(DrawView.this.x, DrawView.this.y));
                    arrayList2.add(new DrawPointInfo(DrawView.this.v, DrawView.this.w, 1.0f));
                    arrayList2.add(new DrawPointInfo(DrawView.this.x, DrawView.this.y, 1.0f));
                    drawInfo.setPointInfos(arrayList);
                    drawInfo.setDrawPointInfos(arrayList2);
                    DrawView.this.J(drawInfo);
                    Paint paint = new Paint();
                    paint.setAlpha(0);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    DrawView.this.mCanvas.drawRect(DrawView.this.v, DrawView.this.w, DrawView.this.x, DrawView.this.y, paint);
                    DrawView.this.T = 1;
                    DrawView.this.invalidate();
                }
            } catch (Error unused) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            } catch (Exception unused2) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            }
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void e() {
            try {
                Bitmap topLayerBitmap = DrawView.this.getTopLayerBitmap();
                if (topLayerBitmap != null) {
                    DrawView drawView = DrawView.this;
                    drawView.bitmap = Bitmap.createBitmap(topLayerBitmap, (int) drawView.v, (int) DrawView.this.w, (int) Math.abs(DrawView.this.x - DrawView.this.v), (int) Math.abs(DrawView.this.y - DrawView.this.w));
                    DrawView.this.T = -1;
                    if (DrawView.this.bitmap == null) {
                        Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
                        return;
                    }
                    DrawView drawView2 = DrawView.this;
                    drawView2.seekBar = (SeekBar) drawView2.getMainActivity().findViewById(R.id.roat_seekbar);
                    DrawView.this.seekBar.setVisibility(0);
                    DrawView.this.seekBar.setProgress(0);
                    DrawView.this.seekBar.setOnSeekBarChangeListener(new DrawSeekBarListener());
                }
            } catch (Error unused) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            } catch (Exception unused2) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            }
        }

        @Override // cn.fjnu.edu.paint.view.AreaSelectDialog.OnSelectListener
        public void f() {
            try {
                Bitmap topLayerBitmap = DrawView.this.getTopLayerBitmap();
                if (topLayerBitmap != null) {
                    DrawView drawView = DrawView.this;
                    drawView.bitmap = Bitmap.createBitmap(topLayerBitmap, (int) drawView.v, (int) DrawView.this.w, (int) Math.abs(DrawView.this.x - DrawView.this.v), (int) Math.abs(DrawView.this.y - DrawView.this.w));
                    DrawView.this.T = -1;
                    if (DrawView.this.bitmap == null) {
                        Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
                        return;
                    }
                    DrawView drawView2 = DrawView.this;
                    drawView2.z = (SeekBar) drawView2.getMainActivity().findViewById(R.id.zoom_seekbar);
                    DrawView.this.z.setVisibility(0);
                    DrawView.this.z.setProgress(50);
                    DrawView.this.z.setOnSeekBarChangeListener(new a());
                }
            } catch (Error unused) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            } catch (Exception unused2) {
                Toast.makeText(DrawView.this.getContext(), (int) R.string.operation_failed_try_again, 0).show();
            }
        }
    }

    /* loaded from: classes.dex */
    public class n implements Consumer<Integer> {

        /* renamed from: a */
        final /* synthetic */ OnDrawPastePhotoFinishedListener f313a;

        n(OnDrawPastePhotoFinishedListener onDrawPastePhotoFinishedListener) {
            DrawView.this = r1;
            this.f313a = onDrawPastePhotoFinishedListener;
        }

        /* renamed from: a */
        public void accept(Integer num) throws Exception {
            DrawView.this.N();
            OnDrawPastePhotoFinishedListener onDrawPastePhotoFinishedListener = this.f313a;
            if (onDrawPastePhotoFinishedListener != null) {
                onDrawPastePhotoFinishedListener.a();
            }
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public class o implements Consumer<Throwable> {

        /* renamed from: a */
        final /* synthetic */ OnDrawPastePhotoFinishedListener f315a;

        o(OnDrawPastePhotoFinishedListener onDrawPastePhotoFinishedListener) {
            DrawView.this = r1;
            this.f315a = onDrawPastePhotoFinishedListener;
        }

        /* renamed from: a */
        public void accept(Throwable th) throws Exception {
            DrawView.this.N();
            OnDrawPastePhotoFinishedListener onDrawPastePhotoFinishedListener = this.f315a;
            if (onDrawPastePhotoFinishedListener != null) {
                onDrawPastePhotoFinishedListener.a();
            }
            DrawView.this.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public static class p {

        /* renamed from: a */
        private final Bitmap f317a;

        /* renamed from: b */
        private final int f318b;

        /* renamed from: c */
        private final int f319c;

        /* renamed from: d */
        private final boolean f320d = true;

        /* renamed from: e */
        private int f321e = 500;

        /* renamed from: f */
        private int[] f322f = new int[500];
        private int[] g = new int[500];

        /* renamed from: h */
        private int f323h;

        public p(Bitmap bitmap) {
            this.f317a = bitmap;
            this.f318b = bitmap.getWidth();
            this.f319c = bitmap.getHeight();
        }

        private void a() {
            while (d() != -1) {
                e();
            }
            this.f323h = 0;
        }

        /* JADX WARNING: Removed duplicated region for block: B:35:0x0067  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void b(int r7, int r8, int r9, int r10) {
            /*
                r6 = this;
                if (r10 != r9) goto L_0x000a
                java.io.PrintStream r7 = java.lang.System.out
                java.lang.String r8 = "do nothing !!!, filled area!!"
                r7.println(r8)
                return
            L_0x000a:
                r6.a()
                r6.f(r7, r8)
            L_0x0010:
                int r7 = r6.d()
                r8 = -1
                if (r7 != r8) goto L_0x0018
                return
            L_0x0018:
                int r8 = r6.e()
            L_0x001c:
                if (r8 < 0) goto L_0x0027
                int r0 = r6.c(r7, r8)
                if (r0 != r10) goto L_0x0027
                int r8 = r8 + -1
                goto L_0x001c
            L_0x0027:
                int r8 = r8 + 1
                r0 = 0
                r1 = 0
                r2 = 0
            L_0x002c:
                int r3 = r6.f319c
                if (r8 >= r3) goto L_0x0010
                int r3 = r6.c(r7, r8)
                if (r3 != r10) goto L_0x0010
                r6.g(r7, r8, r9)
                r3 = 1
                if (r1 != 0) goto L_0x004b
                if (r7 <= 0) goto L_0x004b
                int r4 = r7 + -1
                int r5 = r6.c(r4, r8)
                if (r5 != r10) goto L_0x004b
                r6.f(r4, r8)
                r1 = 1
                goto L_0x0058
            L_0x004b:
                if (r1 == 0) goto L_0x0058
                if (r7 <= 0) goto L_0x0058
                int r4 = r7 + -1
                int r4 = r6.c(r4, r8)
                if (r4 == r10) goto L_0x0058
                r1 = 0
            L_0x0058:
                if (r2 != 0) goto L_0x006c
                int r4 = r6.f318b
                int r4 = r4 - r3
                if (r7 >= r4) goto L_0x006c
                int r4 = r7 + 1
                int r5 = r6.c(r4, r8)
                if (r5 != r10) goto L_0x006c
                r6.f(r4, r8)
                r2 = 1
                goto L_0x007c
            L_0x006c:
                if (r2 == 0) goto L_0x007c
                int r4 = r6.f318b
                int r4 = r4 - r3
                if (r7 >= r4) goto L_0x007c
                int r3 = r7 + 1
                int r3 = r6.c(r3, r8)
                if (r3 == r10) goto L_0x007c
                r2 = 0
            L_0x007c:
                int r8 = r8 + 1
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: cn.fjnu.edu.paint.engine.DrawView.p.b(int, int, int, int):void");
        }

        public int c(int i2, int i3) {
            return this.f317a.getPixel(i2, i3);
        }

        final int d() {
            int i2 = this.f323h;
            if (i2 == 0) {
                return -1;
            }
            return this.f322f[i2 - 1];
        }

        final int e() {
            int[] iArr = this.g;
            int i2 = this.f323h;
            int i3 = iArr[i2 - 1];
            this.f323h = i2 - 1;
            return i3;
        }

        final void f(int i2, int i3) {
            int i4 = this.f323h + 1;
            this.f323h = i4;
            int i5 = this.f321e;
            if (i4 == i5) {
                int[] iArr = new int[i5 * 2];
                int[] iArr2 = new int[i5 * 2];
                System.arraycopy(this.f322f, 0, iArr, 0, i5);
                System.arraycopy(this.g, 0, iArr2, 0, this.f321e);
                this.f322f = iArr;
                this.g = iArr2;
                this.f321e *= 2;
            }
            int[] iArr3 = this.f322f;
            int i6 = this.f323h;
            iArr3[i6 - 1] = i2;
            this.g[i6 - 1] = i3;
        }

        public void g(int i2, int i3, int i4) {
            this.f317a.setPixel(i2, i3, i4);
        }
    }

    public DrawView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.f268a = -16777216;
        this.mPaintMode = 0;
        this.mLastMode = 0;
        int i3 = SharedPreferenceService.C() ? 27 : 7;
        this.f271d = i3;
        this.mShapeValue = i3;
        this.mPenSize = 5.0f;
        this.f277l = true;
        this.m = false;
        this.B = -16777216;
        this.C = -16777216;
        this.D = 5.0f;
        this.paintTextColor = -16777216;
        this.F = 0;
        this.paint = null;
        this.M = 5;
        this.N = true;
        this.mLockObject = new Object();
        this.k0 = 1;
        this.l0 = 0;
        this.m0 = -1.0f;
        this.n0 = -1.0f;
        this.o0 = -1.0f;
        this.p0 = -1.0f;
        this.v0 = -1.0f;
        this.w0 = -1.0f;
        this.y0 = 0;
        this.z0 = new float[]{-1.0f, -1.0f, -1.0f, -1.0f};
        this.context = context;
        o0();
        p0();
    }

    public /* synthetic */ void A0(Throwable th) throws Exception {
        ViewUtils.g(R.string.fill_color_failed);
        N();
        th.printStackTrace();
    }

    private void B0(float f2, float f3) {
        DrawInfo drawInfo;
        float f4 = this.v0;
        if (f4 >= 0.0f) {
            float f5 = this.w0;
            if (f5 >= 0.0f && (drawInfo = this.t0) != null) {
                float f6 = f2 - f4;
                float f7 = f3 - f5;
                List<PointInfo> pointInfos = drawInfo.getPointInfos();
                if (pointInfos != null && pointInfos.size() > 0) {
                    for (PointInfo pointInfo : pointInfos) {
                        pointInfo.setX(pointInfo.getX() + f6);
                        pointInfo.setY(pointInfo.getY() + f7);
                    }
                }
                DrawInfo drawInfo2 = this.t0;
                drawInfo2.setElementLeft(drawInfo2.getElementLeft() + f6);
                DrawInfo drawInfo3 = this.t0;
                drawInfo3.setElementRight(drawInfo3.getElementRight() + f6);
                DrawInfo drawInfo4 = this.t0;
                drawInfo4.setElementTop(drawInfo4.getElementTop() + f7);
                DrawInfo drawInfo5 = this.t0;
                drawInfo5.setElementBottom(drawInfo5.getElementBottom() + f7);
                this.t.reset();
                this.t.addRect(this.t0.getElementLeft(), this.t0.getElementTop(), this.t0.getElementRight(), this.t0.getElementBottom(), Path.Direction.CW);
                this.o = getDrawPathList(this.t0);
                this.paintList = getDrawPaintList(this.t0);
                if (this.y0 != 0) {
                    List<PointInfo> pointInfos2 = this.u0.getPointInfos();
                    if (pointInfos2 != null && pointInfos2.size() > 0) {
                        for (PointInfo pointInfo2 : pointInfos2) {
                            pointInfo2.setX(pointInfo2.getX() + f6);
                            pointInfo2.setY(pointInfo2.getY() + f7);
                        }
                    }
                    DrawInfo drawInfo6 = this.u0;
                    drawInfo6.setElementLeft(drawInfo6.getElementLeft() + f6);
                    DrawInfo drawInfo7 = this.u0;
                    drawInfo7.setElementRight(drawInfo7.getElementRight() + f6);
                    DrawInfo drawInfo8 = this.u0;
                    drawInfo8.setElementTop(drawInfo8.getElementTop() + f7);
                    DrawInfo drawInfo9 = this.u0;
                    drawInfo9.setElementBottom(drawInfo9.getElementBottom() + f7);
                    this.pathList = getDrawPathList(this.u0);
                    this.s = getDrawPaintList(this.u0);
                }
                this.v0 = f2;
                this.w0 = f3;
                invalidate();
            }
        }
    }

    private void C0(List<DrawInfo> list) {
        D0(list, this.mCanvas);
    }

    public void I0() {
        int i2 = this.l0 + 1;
        this.l0 = i2;
        Context context = this.context;
        if (context instanceof PaintMainActivity) {
            PaintMainActivity paintMainActivity = (PaintMainActivity) context;
            if (i2 % 5 == 0) {
                paintMainActivity.A2();
            }
        }
    }

    public void J(DrawInfo drawInfo) {
        synchronized (this.mLockObject) {
            this.d0.addDrawInfo(drawInfo);
            List<DrawInfo> saveDrawInfos = this.d0.getSaveDrawInfos();
            List<DrawInfo> fullImageDrawInfos = this.d0.getFullImageDrawInfos();
            int size = saveDrawInfos.size() - 1;
            ExtendDrawInfo extendDrawInfo = drawInfo.getExtendDrawInfo();
            if (extendDrawInfo != null && extendDrawInfo.isFullImg()) {
                fullImageDrawInfos.add(drawInfo);
                this.d0.setLastFullImgIndex(size);
            } else if (size - this.d0.getLastFullImgIndex() == 50) {
                T0(drawInfo);
                fullImageDrawInfos.add(drawInfo);
                this.d0.setLastFullImgIndex(size);
            }
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            I0();
            return;
        }
        if (this.context instanceof Activity) {
            ((Activity) this.context).runOnUiThread(new e());
        }
    }

    private void L() {
        if (this.m0 >= 0.0f && this.o0 >= 0.0f && this.n0 >= 0.0f && this.p0 >= 0.0f) {
            float a2 = (float) PixeUtils.a(this.context, 40.0f);
            float f2 = this.o0;
            float f3 = this.m0;
            if (f2 - f3 < a2) {
                float f4 = (a2 - (f2 - f3)) / 2.0f;
                this.m0 = f3 - f4;
                this.o0 = f2 + f4;
            }
            float f5 = this.p0;
            float f6 = this.n0;
            if (f5 - f6 < a2) {
                float f7 = (a2 - (f5 - f6)) / 2.0f;
                this.n0 = f6 - f7;
                this.p0 = f5 + f7;
            }
        }
    }

    private void L0() {
        float f2 = this.v;
        float f3 = this.x;
        if (f2 != f3 && this.w != this.y && ((int) Math.abs(f3 - f2)) != 0 && ((int) Math.abs(this.y - this.w)) != 0) {
            if (this.areaSelectDialog == null) {
                this.areaSelectDialog = new AreaSelectDialog(this.context);
            }
            this.areaSelectDialog.setOnSelectListener(new m());
            this.areaSelectDialog.show();
        }
    }

    private void M0() {
        if (this.commonTipDialog == null) {
            AppCommonTipDialog appCommonTipDialog = new AppCommonTipDialog(this.context);
            this.commonTipDialog = appCommonTipDialog;
            appCommonTipDialog.v(this.context.getString(R.string.tip));
            this.commonTipDialog.t(this.context.getString(R.string.save_cloud_sync_tip));
            this.commonTipDialog.setCancelable(false);
            this.commonTipDialog.r(new f());
        }
        this.commonTipDialog.show();
    }

    public void N() {
        AlertDialog alertDialog = this.b0;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.b0.dismiss();
        }
    }

    private void N0() {
        N();
        Activity activity = (Activity) this.context;
        if (activity != null) {
            this.b0 = DialogUtils.b(activity, false);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:247:0x26da  */
    /* JADX WARNING: Removed duplicated region for block: B:306:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void O0(float r26, float r27, float r28) {
        /*
        // Method dump skipped, instructions count: 10316
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.fjnu.edu.paint.engine.DrawView.O0(float, float, float):void");
    }

    private void P() {
        this.q0 = false;
        this.t0 = null;
        this.u0 = null;
        this.o = null;
        this.pathList = null;
        this.paintList = null;
        this.s = null;
        this.p0 = -1.0f;
        this.n0 = -1.0f;
        this.o0 = -1.0f;
        this.m0 = -1.0f;
        invalidate();
    }

    private void P0(float f2, float f3, float f4) {
        Bitmap bitmap;
        if (this.P == null) {
            this.P = new ArrayList(10);
        }
        if (this.R == null) {
            this.R = new ArrayList(10);
        }
        if (this.f271d == 27) {
            if (this.j0 == null) {
                this.j0 = new ArrayList(10);
            }
            if (this.path == null) {
                this.path = new Path();
            }
            this.j0.clear();
        }
        this.P.clear();
        this.R.clear();
        Path path = this.o.get(0);
        Path path2 = this.pathList.get(0);
        int i2 = this.mPaintMode;
        if (i2 == 0) {
            switch (this.f271d) {
                case 0:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                    this.f277l = true;
                    path.moveTo(f2, f3);
                    if (this.y0 != 0) {
                        path2.moveTo(k0(f2), l0(f3));
                    }
                    this.v = f2;
                    this.w = f3;
                    return;
                case 1:
                    if (this.f277l) {
                        path.addCircle(f2, f3, 0.5f, Path.Direction.CW);
                        this.P.add(new PointInfo(f2, f3));
                        this.R.add(new DrawPointInfo(f2, f3, f4));
                        if (this.y0 != 0) {
                            path2.addCircle(k0(f2), l0(f3), 0.5f, Path.Direction.CW);
                            this.Q.add(new PointInfo(k0(f2), l0(f3)));
                            this.S.add(new DrawPointInfo(k0(f2), l0(f3), f4));
                        }
                        BrokenPoint brokenPoint = new BrokenPoint();
                        this.g = brokenPoint;
                        this.f277l = false;
                        brokenPoint.pointX = f2;
                        brokenPoint.pointY = f3;
                        J(e0(0, 1));
                        if (this.y0 != 0) {
                            J(f0(0, 1, true));
                            return;
                        }
                        return;
                    }
                    BrokenPoint brokenPoint2 = this.g;
                    path.moveTo(brokenPoint2.pointX, brokenPoint2.pointY);
                    path.lineTo(f2, f3);
                    List<PointInfo> list = this.P;
                    BrokenPoint brokenPoint3 = this.g;
                    list.add(new PointInfo(brokenPoint3.pointX, brokenPoint3.pointY));
                    this.P.add(new PointInfo(f2, f3));
                    List<DrawPointInfo> list2 = this.R;
                    BrokenPoint brokenPoint4 = this.g;
                    list2.add(new DrawPointInfo(brokenPoint4.pointX, brokenPoint4.pointY, f4));
                    this.R.add(new DrawPointInfo(f2, f3, f4));
                    J(e0(0, 1));
                    if (this.y0 != 0) {
                        path2.moveTo(k0(this.g.pointX), l0(this.g.pointY));
                        path2.lineTo(k0(f2), l0(f3));
                        this.P.add(new PointInfo(k0(this.g.pointX), l0(this.g.pointY)));
                        this.P.add(new PointInfo(k0(f2), l0(f3)));
                        this.R.add(new DrawPointInfo(k0(this.g.pointX), l0(this.g.pointY), f4));
                        this.R.add(new DrawPointInfo(k0(f2), l0(f3), f4));
                        J(e0(0, 1));
                    }
                    BrokenPoint brokenPoint5 = this.g;
                    brokenPoint5.pointX = f2;
                    brokenPoint5.pointY = f3;
                    return;
                case 4:
                case 13:
                    this.f277l = true;
                    this.f274h = f2;
                    this.f275i = f3;
                    return;
                default:
                    return;
            }
        } else if (i2 == 1) {
            this.v = f2;
            this.w = f3;
            path.moveTo(f2, f3);
        } else if (i2 == 2) {
            this.v = f2;
            this.w = f3;
            path.moveTo(f2, f3);
            int i3 = this.T;
            if (i3 == -1) {
                path.moveTo(f2, f3);
                this.v = f2;
                this.w = f3;
            } else if ((i3 == 0 || i3 == 1) && (bitmap = this.bitmap) != null) {
                this.mCanvas.drawBitmap(bitmap, f2 - (((float) bitmap.getWidth()) * 0.5f), f3 - (((float) this.bitmap.getHeight()) * 0.5f), this.layerPaint);
                invalidate();
                N0();
                this.c0 = Observable.h(1).i(new i()).s(Schedulers.b()).l(AndroidSchedulers.a()).p(new g(), new h());
            }
        }
    }

    private RectF Q(float f2, float f3, float f4, float f5, Path path, Path path2) {
        path.reset();
        float abs = Math.abs(f2 - f4) * 2.0f;
        float f6 = (2.0f * f2) - f4;
        float min = Math.min(f4, f6);
        path.moveTo(f2, f3);
        path.lineTo(f4, f5);
        path.moveTo(f2, f3);
        path.lineTo(f6, f5);
        float f7 = 0.18f * abs;
        float f8 = f5 - f7;
        float f9 = min + abs;
        float f10 = f7 + f5;
        path.arcTo(min, f8, f9, f10, 0.0f, 180.0f, true);
        path2.reset();
        path2.moveTo(min, f5);
        path2.arcTo(min, f8, f9, f10, 180.0f, 180.0f, false);
        if (f5 > f3) {
            return new RectF(min, f3, f9, f10);
        }
        return new RectF(min, f8, f9, f3);
    }

    private void Q0(float f2, float f3) {
        Bitmap bitmap;
        List<Paint> list = this.paintList;
        if (list == null || list.size() == 0) {
            q0();
        }
        List<Path> list2 = this.o;
        if (list2 == null || list2.size() == 0) {
            r0();
        }
        int i2 = 0;
        Path path = this.o.get(0);
        this.pathList.get(0);
        int i3 = this.mPaintMode;
        if (i3 == 0) {
            int i4 = this.f271d;
            switch (i4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 28:
                case 29:
                case 30:
                case 31:
                    DrawInfo e0 = e0(0, i4);
                    this.t0 = e0;
                    if (this.y0 != 0) {
                        this.u0 = f0(0, this.f271d, true);
                    }
                    int i5 = this.f271d;
                    if (i5 == 1 || i5 == 7 || this.x0) {
                        if (this.o.size() == this.paintList.size()) {
                            for (int i6 = 0; i6 < this.o.size(); i6++) {
                                this.mCanvas.drawPath(this.o.get(i6), this.paintList.get(i6));
                            }
                            if (!(this.y0 == 0 || this.pathList == null)) {
                                while (i2 < this.pathList.size()) {
                                    this.mCanvas.drawPath(this.pathList.get(i2), this.s.get(i2));
                                    i2++;
                                }
                            }
                            invalidate();
                        }
                        J(e0);
                        this.o = null;
                        this.pathList = null;
                        return;
                    }
                    this.q0 = true;
                    this.v0 = -1.0f;
                    this.w0 = -1.0f;
                    this.t.reset();
                    this.t.addRect(e0.getElementLeft(), e0.getElementTop(), e0.getElementRight(), e0.getElementBottom(), Path.Direction.CW);
                    return;
                case 27:
                    invalidate();
                    J(e0(0, this.f271d));
                    this.o = null;
                    this.pathList = null;
                    return;
                default:
                    return;
            }
        } else if (i3 == 1) {
            if (this.o.size() == this.paintList.size()) {
                while (i2 < this.o.size()) {
                    this.mCanvas.drawPath(this.o.get(i2), this.paintList.get(i2));
                    i2++;
                }
            }
            J(e0(1, 7));
            this.o = null;
            this.pathList = null;
        } else if (i3 != 2) {
            if (i3 != 4) {
                this.o = null;
                this.pathList = null;
                return;
            }
            try {
                bitmap = this.d0.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            } catch (Error | Exception e2) {
                e2.printStackTrace();
                bitmap = null;
            }
            if (bitmap == null) {
                ViewUtils.g(R.string.fill_color_failed);
                System.gc();
            } else if (((Activity) this.context) != null) {
                N0();
                Flowable.g(bitmap).h(new Function(f2, f3) { // from class: b.f

                    /* renamed from: b  reason: collision with root package name */
                    public final /* synthetic */ float f119b;

                    /* renamed from: c  reason: collision with root package name */
                    public final /* synthetic */ float f120c;

                    {
                        this.f119b = r2;
                        this.f120c = r3;
                    }

                    @Override // io.reactivex.functions.Function
                    public final Object apply(Object obj) {
                        return DrawView.a(DrawView.this, this.f119b, this.f120c, (Bitmap) obj);
                    }
                }).x(Schedulers.a()).l(AndroidSchedulers.a()).t(new Consumer() { // from class: b.d
                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        DrawView.c(DrawView.this, (int[]) obj);
                    }
                }, new Consumer() { // from class: b.c
                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        DrawView.e(DrawView.this, (Throwable) obj);
                    }
                });
                this.o = null;
                this.pathList = null;
            }
        } else if (this.T == -1) {
            float f4 = this.v;
            float f5 = this.x;
            if (f4 == f5 || this.w == this.y) {
                ViewUtils.g(R.string.sel_correct_area_tip);
            } else if (((int) Math.abs(f5 - f4)) == 0 || ((int) Math.abs(this.y - this.w)) == 0) {
                ViewUtils.g(R.string.sel_correct_area_tip);
            } else {
                LayerInfo layerInfo = this.d0;
                if (layerInfo == null || layerInfo.getBitmap() == null) {
                    ViewUtils.g(R.string.operation_failed_try_again);
                    return;
                }
                float f6 = this.v;
                float f7 = this.x;
                if (f6 > f7) {
                    this.v = f7;
                    this.x = f6;
                }
                float f8 = this.w;
                float f9 = this.y;
                if (f8 > f9) {
                    this.w = f9;
                    this.y = f8;
                }
                L0();
                path.reset();
                this.o = null;
                this.pathList = null;
            }
        }
    }

    private RectF R(float f2, float f3, float f4, float f5, Path path, Path path2) {
        path.reset();
        float sqrt = ((float) Math.sqrt(Math.pow((double) (f4 - f2), 2.0d) + Math.pow((double) (f5 - f3), 2.0d))) * 1.414f;
        float f6 = sqrt / 2.0f;
        float f7 = f2 - f6;
        float f8 = f3 - f6;
        path.moveTo(f7, f8);
        float f9 = f7 + sqrt;
        path.lineTo(f9, f8);
        float f10 = f8 + sqrt;
        path.lineTo(f9, f10);
        path.lineTo(f7, f10);
        path.lineTo(f7, f8);
        float f11 = 0.376f * sqrt;
        float f12 = f7 + f11;
        float f13 = f8 - f11;
        path.lineTo(f12, f13);
        float f14 = f12 + sqrt;
        path.lineTo(f14, f13);
        path.lineTo(f9, f8);
        path.moveTo(f14, f13);
        float f15 = sqrt + f13;
        path.lineTo(f14, f15);
        path.lineTo(f9, f10);
        path2.reset();
        path2.moveTo(f12, f13);
        path2.lineTo(f12, f15);
        path2.lineTo(f14, f15);
        path2.moveTo(f12, f15);
        path2.lineTo(f7, f10);
        return new RectF(f7, f13, f14, f10);
    }

    private void R0() {
        List<PointInfo> pointInfos;
        List<DrawInfo> saveDrawInfos = this.d0.getSaveDrawInfos();
        if (saveDrawInfos.size() > 0) {
            DrawInfo drawInfo = saveDrawInfos.get(saveDrawInfos.size() - 1);
            if (drawInfo.getMode() == 0 && drawInfo.getShapeType() == 1 && this.g != null && (pointInfos = drawInfo.getPointInfos()) != null && pointInfos.size() > 0) {
                if (pointInfos.size() == 1) {
                    this.g.pointX = pointInfos.get(0).getX();
                    this.g.pointY = pointInfos.get(0).getY();
                } else if (pointInfos.size() == 2) {
                    this.g.pointX = pointInfos.get(1).getX();
                    this.g.pointY = pointInfos.get(1).getY();
                }
            }
        }
        if (saveDrawInfos.size() == 0) {
            H0();
            invalidate();
            return;
        }
        List<DrawInfo> fullImageDrawInfos = this.d0.getFullImageDrawInfos();
        if (fullImageDrawInfos != null && fullImageDrawInfos.size() > 0) {
            int indexOf = saveDrawInfos.indexOf(fullImageDrawInfos.get(fullImageDrawInfos.size() - 1));
            this.d0.setLastFullImgIndex(indexOf);
            if (indexOf >= 0) {
                C0(saveDrawInfos.subList(indexOf, saveDrawInfos.size()));
                H0();
                invalidate();
                return;
            }
        }
        C0(saveDrawInfos);
        H0();
        invalidate();
    }

    private RectF S(float f2, float f3, float f4, float f5, Path path, Path path2) {
        path.reset();
        float min = Math.min(f2, f4);
        float min2 = Math.min(f3, f5);
        float abs = Math.abs(f5 - f3);
        float abs2 = Math.abs(f4 - f2);
        path.moveTo(min, min2);
        float f6 = min + abs2;
        path.lineTo(f6, min2);
        float f7 = min2 + abs;
        path.lineTo(f6, f7);
        path.lineTo(min, f7);
        path.lineTo(min, min2);
        float f8 = abs / 1.414f;
        float f9 = min + f8;
        float f10 = min2 - f8;
        path.lineTo(f9, f10);
        float f11 = abs2 + f9;
        path.lineTo(f11, f10);
        path.lineTo(f6, min2);
        path.moveTo(f11, f10);
        float f12 = abs + f10;
        path.lineTo(f11, f12);
        path.lineTo(f6, f7);
        path2.reset();
        path2.moveTo(f9, f10);
        path2.lineTo(f9, f12);
        path2.lineTo(f11, f12);
        path2.moveTo(f9, f12);
        path2.lineTo(min, f7);
        return new RectF(min, f10, f11, f7);
    }

    private RectF T(float f2, float f3, float f4, float f5, Path path, Path path2) {
        path.reset();
        float min = Math.min(f2, f4);
        float min2 = Math.min(f3, f5);
        float abs = Math.abs(f5 - f3);
        float abs2 = Math.abs(f4 - f2);
        path.moveTo(min, min2);
        float f6 = min2 + abs;
        path.lineTo(min, f6);
        float f7 = abs2 / 4.0f;
        float f8 = f6 - f7;
        float f9 = min + abs2;
        float f10 = f6 + f7;
        path.arcTo(min, f8, f9, f10, 0.0f, 180.0f, true);
        path.moveTo(f9, f6);
        path.lineTo(f9, min2);
        float f11 = min2 - f7;
        path.addOval(min, f11, f9, min2 + f7, Path.Direction.CW);
        path2.reset();
        path2.moveTo(min, f6);
        path2.arcTo(min, f8, f9, f10, 180.0f, 180.0f, false);
        return new RectF(min, f11, f9, f10);
    }

    public void T0(DrawInfo drawInfo) {
        drawInfo.setImgX(0.0f);
        drawInfo.setImgY(0.0f);
        Bitmap bitmap = this.d0.getBitmap();
        drawInfo.setImgWidth(bitmap.getWidth());
        drawInfo.setImgHeight(bitmap.getHeight());
        drawInfo.saveImgDataToFile(bitmap, true);
    }

    private RectF U(float f2, float f3, float f4, float f5, Path path) {
        RectF rectF;
        path.reset();
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        double d2 = (double) ((f2 + f4) / 2.0f);
        double d3 = (double) ((f3 + f5) / 2.0f);
        double sqrt = Math.sqrt(Math.pow((double) f6, 2.0d) + Math.pow((double) f7, 2.0d)) / 2.0d;
        float f8 = (float) (d2 - sqrt);
        float f9 = (float) (d3 - sqrt);
        float f10 = (float) (d2 + sqrt);
        float f11 = (float) (d3 + sqrt);
        if (f3 < f5) {
            if (f2 > f4) {
                RectF rectF2 = new RectF(f8, f9, f2, f5);
                path.addArc(f8, f9, f10, f11, ((float) ((Math.atan((double) (f7 / f6)) * 180.0d) / 3.141592653589793d)) + 180.0f, 180.0f);
                return rectF2;
            }
            rectF = new RectF(f8, f3, f4, f11);
            path.addArc(f8, f9, f10, f11, (float) ((Math.atan((double) (f7 / f6)) * 180.0d) / 3.141592653589793d), 180.0f);
        } else if (f2 > f4) {
            rectF = new RectF(f4, f9, f10, f3);
            path.addArc(f8, f9, f10, f11, ((float) ((Math.atan((double) (f7 / f6)) * 180.0d) / 3.141592653589793d)) - 0.024902344f, 180.0f);
        } else {
            RectF rectF3 = new RectF(f2, f5, f10, f11);
            path.addArc(f8, f9, f10, f11, (float) ((Math.atan((double) (f7 / f6)) * 180.0d) / 3.141592653589793d), 180.0f);
            return rectF3;
        }
        return rectF;
    }

    private void V(float f2, float f3, float f4, float f5, Path path) {
        float f6;
        float f7;
        float f8;
        float f9;
        float f10 = f5 - f3;
        ArrayList arrayList = new ArrayList(4);
        ArrayList arrayList2 = new ArrayList(4);
        float f11 = (f2 + f4) / 2.0f;
        arrayList.add(new PointF(f11, f5));
        float f12 = (0.3125f * f10) + f3;
        arrayList.add(new PointF(f2, f12));
        float f13 = (f4 - f2) * 0.2f;
        arrayList.add(new PointF(f2 + f13, f3));
        float f14 = (f10 * 0.1f) + f3;
        arrayList.add(new PointF(f11, f14));
        arrayList2.add(new PointF(f11, f5));
        arrayList2.add(new PointF(f4, f12));
        arrayList2.add(new PointF(f4 - f13, f3));
        arrayList2.add(new PointF(f11, f14));
        ArrayList<List> arrayList3 = new ArrayList();
        arrayList3.add(arrayList);
        arrayList3.add(arrayList2);
        for (List list : arrayList3) {
            path.moveTo(((PointF) list.get(0)).x, ((PointF) list.get(0)).y);
            PointF pointF = (PointF) list.get(list.size() - 1);
            int i2 = 0;
            while (i2 < list.size() - 1) {
                int i3 = i2 + 1;
                PointF pointF2 = (PointF) list.get(i3);
                if (i2 == 0) {
                    f7 = ((PointF) list.get(i2)).x + ((((PointF) list.get(i3)).x - ((PointF) list.get(0)).x) * 0.16f);
                    f6 = ((PointF) list.get(i2)).y + ((((PointF) list.get(i3)).y - ((PointF) list.get(0)).y) * 0.16f);
                } else {
                    int i4 = i2 - 1;
                    f7 = ((PointF) list.get(i2)).x + ((((PointF) list.get(i3)).x - ((PointF) list.get(i4)).x) * 0.16f);
                    f6 = ((PointF) list.get(i2)).y + ((((PointF) list.get(i3)).y - ((PointF) list.get(i4)).y) * 0.16f);
                }
                if (i2 == list.size() - 2) {
                    float f15 = pointF.x;
                    f9 = f15 - ((f15 - ((PointF) list.get(i2)).x) * 0.16f);
                    float f16 = pointF.y;
                    f8 = f16 - (0.16f * (f16 - ((PointF) list.get(i2)).y));
                } else {
                    int i5 = i2 + 2;
                    f9 = ((PointF) list.get(i3)).x - ((((PointF) list.get(i5)).x - ((PointF) list.get(i2)).x) * 0.16f);
                    f8 = ((PointF) list.get(i3)).y - (0.16f * (((PointF) list.get(i5)).y - ((PointF) list.get(i2)).y));
                }
                path.cubicTo(f7, f6, f9, f8, pointF2.x, pointF2.y);
                i2 = i3;
            }
        }
    }

    private RectF W(float f2, float f3, float f4, float f5, boolean z, Path path) {
        path.reset();
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        double sqrt = Math.sqrt((double) ((f6 * f6) + (f7 * f7)));
        double d2 = (double) f2;
        float f8 = (float) (d2 - sqrt);
        double d3 = (double) f3;
        float f9 = (float) (d3 - sqrt);
        float f10 = (float) (d2 + sqrt);
        float f11 = (float) (d3 + sqrt);
        float acos = (float) ((Math.acos(((double) Math.abs(f6)) / sqrt) * 360.0d) / 3.141592653589793d);
        float f12 = 360.0f - (acos / 2.0f);
        int i2 = (f4 > f2 ? 1 : (f4 == f2 ? 0 : -1));
        if (i2 < 0) {
            f12 += 180.0f;
        }
        path.addArc(f8, f9, f10, f11, f12, acos);
        if (!z) {
            path.moveTo(f2, f3);
            path.lineTo(f4, f5);
            path.moveTo(f2, f3);
            path.lineTo(f4, (f3 * 2.0f) - f5);
        } else {
            path.moveTo(f2, f3);
            path.lineTo(f4, f5);
            path.lineTo(f4, (f3 * 2.0f) - f5);
            path.close();
        }
        if (i2 < 0) {
            return new RectF(f8, f3 - Math.abs(f7), (float) (((double) f8) + sqrt), f3 + Math.abs(f7));
        }
        return new RectF(f2, f3 - Math.abs(f7), f10, f3 + Math.abs(f7));
    }

    private void drawLayers(Canvas canvas) {
        List<LayerInfo> list = this.e0;
        if (list != null && list.size() > 0) {
            for (LayerInfo layerInfo : this.e0) {
                if (!layerInfo.isHide()) {
                    this.layerPaint.setAlpha(layerInfo.getLayerAlpha());
                    canvas.drawBitmap(layerInfo.getBitmap(), 0.0f, 0.0f, this.layerPaint);
                    this.layerPaint.setAlpha(255);
                }
            }
        }
    }

    private RectF Z(float f2, float f3, float f4, float f5, boolean z, Path path) {
        path.reset();
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        double sqrt = Math.sqrt((double) ((f6 * f6) + (f7 * f7)));
        double d2 = (double) f2;
        float f8 = (float) (d2 - sqrt);
        double d3 = (double) f3;
        float f9 = (float) (d3 - sqrt);
        float f10 = (float) (d2 + sqrt);
        float f11 = (float) (d3 + sqrt);
        float acos = (float) ((Math.acos(((double) Math.abs(f7)) / sqrt) * 360.0d) / 3.141592653589793d);
        float f12 = (180.0f - acos) / 2.0f;
        int i2 = (f5 > f3 ? 1 : (f5 == f3 ? 0 : -1));
        if (i2 < 0) {
            f12 += 180.0f;
        }
        path.addArc(f8, f9, f10, f11, f12, acos);
        if (!z) {
            path.moveTo(f2, f3);
            path.lineTo(f4, f5);
            path.moveTo(f2, f3);
            path.lineTo((f2 * 2.0f) - f4, f5);
        } else {
            path.moveTo(f2, f3);
            path.lineTo(f4, f5);
            path.lineTo((f2 * 2.0f) - f4, f5);
            path.close();
        }
        if (i2 < 0) {
            return new RectF(f2 - Math.abs(f6), f9, f2 + Math.abs(f6), f3);
        }
        return new RectF(f2 - Math.abs(f6), f3, f2 + Math.abs(f6), f11);
    }

    private RectF a0(float f2, float f3, float f4, float f5, Path path, Path path2) {
        path.reset();
        float sqrt = (float) Math.sqrt(Math.pow((double) (f2 - f4), 2.0d) + Math.pow((double) (f3 - f5), 2.0d));
        path.addCircle(f2, f3, sqrt, Path.Direction.CW);
        path2.reset();
        float f6 = f2 - sqrt;
        float f7 = 0.222f * sqrt;
        float f8 = f2 + sqrt;
        path2.addOval(f6, f3 + f7, f8, f3 - f7, Path.Direction.CW);
        return new RectF(f6, f3 - sqrt, f8, f3 + sqrt);
    }

    private RectF b0(float f2, float f3, float f4, float f5, Path path) {
        path.moveTo(f2, f3);
        path.lineTo(f4, f3);
        float f6 = (f2 + f4) / 2.0f;
        path.moveTo(f6, f3);
        path.lineTo(f6, f5);
        RectF rectF = new RectF();
        rectF.left = Math.min(f2, f4);
        rectF.top = Math.min(f3, f5);
        rectF.right = Math.max(f2, f4);
        rectF.bottom = Math.max(f3, f5);
        return rectF;
    }

    private void d0(Bitmap bitmap, Point point, int i2, int i3) {
        new p(bitmap).b(point.x, point.y, i3, i2);
    }

    private DrawInfo e0(int i2, int i3) {
        return f0(i2, i3, false);
    }

    private DrawInfo f0(int i2, int i3, boolean z) {
        List<Paint> list;
        List<Paint> list2 = this.paintList;
        if (list2 == null || list2.size() == 0) {
            return null;
        }
        boolean z2 = false;
        Paint paint = this.paintList.get(0);
        if (z && (list = this.s) != null && list.size() > 0) {
            paint = this.s.get(0);
        }
        DrawInfo drawInfo = new DrawInfo();
        drawInfo.setPaintColor(paint.getColor());
        drawInfo.setPaintSize(paint.getStrokeWidth());
        drawInfo.setShapeType(i3);
        drawInfo.setMode(i2);
        drawInfo.setLightEffect(false);
        if (paint.getStyle() == Paint.Style.FILL) {
            z2 = true;
        }
        drawInfo.setFillMode(z2);
        if (!z) {
            drawInfo.setElementLeft(this.m0);
            drawInfo.setElementTop(this.n0);
            drawInfo.setElementRight(this.o0);
            drawInfo.setElementBottom(this.p0);
            drawInfo.setPointInfos(new ArrayList(this.P));
            drawInfo.setDrawPointInfos(new ArrayList(this.R));
        } else {
            drawInfo.setElementLeft(k0(this.m0));
            drawInfo.setElementTop(l0(this.n0));
            drawInfo.setElementRight(k0(this.o0));
            drawInfo.setElementBottom(l0(this.p0));
            drawInfo.setPointInfos(new ArrayList(this.Q));
            drawInfo.setDrawPointInfos(new ArrayList(this.S));
        }
        ExtendDrawInfo extendDrawInfo = new ExtendDrawInfo();
        if (paint.getPathEffect() instanceof DashPathEffect) {
            extendDrawInfo.setLineType(2);
        } else {
            extendDrawInfo.setLineType(1);
        }
        extendDrawInfo.setPenType(BaseGlobalValue.currentPenType);
        drawInfo.setCustomBrushImgPath(BaseGlobalValue.u);
        drawInfo.setText(new Gson().toJson(extendDrawInfo));
        return drawInfo;
    }

    private int getDrawHeight() {
        MainCanvasOP mainActivity = getMainActivity();
        if (mainActivity == null) {
            return 0;
        }
        return mainActivity.e();
    }

    private int getDrawWidth() {
        MainCanvasOP mainActivity = getMainActivity();
        if (mainActivity == null) {
            return 0;
        }
        return mainActivity.g();
    }

    public Bitmap getTopLayerBitmap() {
        LayerInfo layerInfo = this.d0;
        if (layerInfo == null) {
            return null;
        }
        return layerInfo.getBitmap();
    }

    private float k0(float f2) {
        return this.y0 == 1 ? (this.z0[0] * 2.0f) - f2 : f2;
    }

    private float l0(float f2) {
        if (this.y0 == 1) {
            return f2;
        }
        return (this.z0[1] * 2.0f) - f2;
    }

    private void n0() {
        ZoomControls zoomControls;
        Context context = this.context;
        if (context instanceof PaintMainActivity) {
            zoomControls = ((PaintMainActivity) context).M1();
        } else {
            zoomControls = context instanceof PaperPaintMainActivity2 ? ((PaperPaintMainActivity2) context).D1() : null;
        }
        if (zoomControls != null) {
            zoomControls.setVisibility(4);
        }
    }

    private void o0() {
        int a2 = PixeUtils.a(this.context, 20.0f);
        int a3 = PixeUtils.a(this.context, 20.0f);
        this.r0 = BitmapUtils.e(this.context, R.drawable.element_delete, a2, a2);
        this.s0 = BitmapUtils.e(this.context, R.drawable.element_confirm, a3, a3);
        this.colorSelectInfo = new ColorSelectInfo(1, -16777216, -16777216, -16777216, AppUtils.a(), -16777216);
        if (this.u == null) {
            Paint paint = new Paint();
            this.u = paint;
            paint.setAntiAlias(true);
            this.u.setDither(true);
            this.u.setColor(-16777216);
            this.u.setPathEffect(new DashPathEffect(new float[]{5.0f, 10.0f}, 0.0f));
            this.u.setStrokeWidth(2.0f);
            this.u.setStyle(Paint.Style.STROKE);
        }
        if (this.t == null) {
            this.t = new Path();
        }
    }

    private void p0() {
        A0 = false;
        setOnTouchListener(new View.OnTouchListener() { // from class: b.a
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return DrawView.g(DrawView.this, view, motionEvent);
            }
        });
    }

    private void r0() {
        List<Path> list = this.o;
        if (list != null) {
            list.clear();
        }
        List<Path> list2 = this.pathList;
        if (list2 != null) {
            list2.clear();
        }
        this.o = new ArrayList();
        this.o.add(new Path());
        this.pathList = new ArrayList();
        this.pathList.add(new Path());
    }

    private boolean t0() {
        return this.N;
    }

    public /* synthetic */ boolean u0(View view, MotionEvent motionEvent) {
        return !t0();
    }

    public /* synthetic */ Boolean v0(File file, String str, MainCanvasOP mainCanvasOP, Bitmap bitmap) throws Exception {
        Exception e2;
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
        boolean z = true;
        if (SharedPreferenceService.s() == 1) {
            compressFormat = Bitmap.CompressFormat.JPEG;
        }
        try {
            String substring = file.getAbsolutePath().substring(Environment.getExternalStorageDirectory().getAbsolutePath().length());
            if (substring.startsWith("/")) {
                substring = new File(substring.substring(1)).getParent();
            }
            File file2 = new File(x.a().getFilesDir(), substring);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (Build.VERSION.SDK_INT <= 29) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(compressFormat, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } else {
                ContentResolver contentResolver = x.a().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("relative_path", substring);
                if (compressFormat == Bitmap.CompressFormat.JPEG) {
                    contentValues.put("mime_type", "image/jpeg");
                } else {
                    contentValues.put("mime_type", "image/png");
                }
                contentValues.put("_display_name", file.getName());
                contentValues.put("is_pending", (Integer) 1);
                Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                OutputStream openOutputStream = contentResolver.openOutputStream(insert, "w");
                bitmap.compress(compressFormat, 100, openOutputStream);
                openOutputStream.flush();
                openOutputStream.close();
                contentValues.clear();
                contentValues.put("is_pending", (Integer) 0);
                contentResolver.update(insert, contentValues, null, null);
            }
        } catch (Exception e3) {
            e2 = e3;
            z = false;
        }
        try {
            File j2 = BaseAppUtils.j();
            String b2 = BaseAppUtils.b();
            if (!TextUtils.isEmpty(b2)) {
                j2 = new File(j2, b2);
            }
            if (!j2.exists()) {
                j2.mkdirs();
            }
            String name = new File(str).getName();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(j2, name.substring(0, name.lastIndexOf(".")) + ".obj"), false));
            CanvasInfo canvasInfo = new CanvasInfo();
            Drawable drawable = getDrawable();
            if (drawable instanceof ColorDrawable) {
                canvasInfo.setBackgroundType(2);
                canvasInfo.setBackgroundColor(((ColorDrawable) drawable).getColor());
            } else if (drawable instanceof BitmapDrawable) {
                canvasInfo.setBackgroundType(1);
                Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
                canvasInfo.setBackgroundData(BitmapUtils.b(bitmap2));
                canvasInfo.setBackgroundWith(bitmap2.getWidth());
                canvasInfo.setBackgroundHeight(bitmap2.getHeight());
            }
            canvasInfo.setCanvasWidth(mainCanvasOP.g());
            canvasInfo.setCanvasHeight(mainCanvasOP.e());
            objectOutputStream.writeObject(canvasInfo);
            File file3 = new File(x.a().getFilesDir(), "CacheImg");
            if (!file3.exists()) {
                file3.mkdirs();
            }
            for (int i2 = 0; i2 < this.e0.size(); i2++) {
                LayerInfo layerInfo = this.e0.get(i2);
                if (layerInfo != null) {
                    try {
                        Bitmap bitmap3 = layerInfo.getBitmap();
                        File file4 = new File(file3, UUID.randomUUID().toString() + ".png");
                        BitmapUtils.h(bitmap3, file4.getAbsolutePath());
                        layerInfo.setLayerPath(file4.getAbsolutePath());
                        objectOutputStream.writeObject(layerInfo);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            }
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e5) {
            e2 = e5;
            e2.printStackTrace();
            return Boolean.valueOf(z);
        }
        return Boolean.valueOf(z);
    }

    public /* synthetic */ void w0(OnSavePhotoListener onSavePhotoListener, boolean z, String str, int i2, Boolean bool) throws Exception {
        if (onSavePhotoListener != null) {
            onSavePhotoListener.a(bool.booleanValue());
        }
        if (bool.booleanValue()) {
            if (Build.VERSION.SDK_INT <= 29 && z) {
                MediaScannerConnection.scanFile(this.context, new String[]{str}, null, null);
            }
            if (i2 == 1) {
                ViewUtils.h(this.context.getString(R.string.file_save_to, str));
                if (!SharedPreferenceService.M() && !BaseAppUtils.s()) {
                    SharedPreferenceService.o0(true);
                    M0();
                }
            }
            UserInfo z2 = SharedPreferenceService.z();
            if (z2 != null) {
                String accountId = z2.getAccountId();
                if (!TextUtils.isEmpty(accountId)) {
                    String e2 = Md5Utils.e(accountId);
                    if (!TextUtils.isEmpty(e2) && BaseAppUtils.s()) {
                        File file = new File(str);
                        HuaweiStorageManagerService e3 = HuaweiStorageManagerService.e();
                        Context context = this.context;
                        e3.h(context, "user_photo_1/" + e2 + "/" + file.getName(), file.getAbsolutePath());
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        ViewUtils.g(R.string.save_file_error);
    }

    public static /* synthetic */ void x0(OnSavePhotoListener onSavePhotoListener, Throwable th) throws Exception {
        th.printStackTrace();
        ViewUtils.g(R.string.save_file_error);
        if (onSavePhotoListener != null) {
            onSavePhotoListener.a(false);
        }
    }

    public /* synthetic */ int[] y0(float f2, float f3, Bitmap bitmap) throws Exception {
        int i2 = (int) f2;
        int i3 = (int) f3;
        d0(bitmap, new Point(i2, i3), bitmap.getPixel(i2, i3), this.C);
        try {
            int[] iArr = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return iArr;
        } catch (Error | Exception e2) {
            e2.printStackTrace();
            System.gc();
            return new int[0];
        }
    }

    public /* synthetic */ void z0(int[] iArr) throws Exception {
        if (iArr == null || iArr.length == 0) {
            ViewUtils.g(R.string.fill_color_failed);
            N();
            return;
        }
        Bitmap bitmap = this.d0.getBitmap();
        bitmap.setPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.c0 = Observable.h(1).i(new l()).s(Schedulers.b()).l(AndroidSchedulers.a()).p(new j(), new k());
    }

    public void D0(List<DrawInfo> list, Canvas canvas) {
        Bitmap imgFromFile;
        List<PointInfo> pointInfos;
        Bitmap imgFromFile2;
        for (DrawInfo drawInfo : list) {
            ExtendDrawInfo extendDrawInfo = drawInfo.getExtendDrawInfo();
            if (extendDrawInfo == null || !extendDrawInfo.isFullImg()) {
                if (drawInfo.getMode() == 0 || drawInfo.getMode() == 1) {
                    List<Path> pathList1 = getDrawPathList(drawInfo);
                    List<Paint> drawPaintList = getDrawPaintList(drawInfo);
                    if (!(pathList1 == null || pathList1.size() == 0 || drawPaintList == null || drawPaintList.size() == 0 || pathList1.size() != drawPaintList.size())) {
                        for (int i2 = 0; i2 < pathList1.size(); i2++) {
                            canvas.drawPath(pathList1.get(i2), drawPaintList.get(i2));
                        }
                    }
                } else if (drawInfo.getMode() == 5) {
                    Bitmap imgFromFile3 = drawInfo.getImgFromFile();
                    if (imgFromFile3 != null) {
                        canvas.drawBitmap(imgFromFile3, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                    }
                } else if (drawInfo.getMode() == 6) {
                    Bitmap imgFromFile4 = drawInfo.getImgFromFile();
                    if (imgFromFile4 != null) {
                        Matrix matrix = new Matrix();
                        matrix.setValues(drawInfo.getMartixValues());
                        canvas.drawBitmap(imgFromFile4, matrix, this.layerPaint);
                        if (!imgFromFile4.isRecycled()) {
                            imgFromFile4.recycle();
                        }
                    }
                } else if (drawInfo.getMode() == 2) {
                    Paint paint = new Paint();
                    paint.setAlpha(0);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    if (drawInfo.getImgType() == 6) {
                        Bitmap imgFromFile5 = drawInfo.getImgFromFile();
                        if (imgFromFile5 != null) {
                            canvas.drawBitmap(imgFromFile5, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                            if (!imgFromFile5.isRecycled()) {
                                imgFromFile5.recycle();
                            }
                        }
                    } else if (drawInfo.getImgType() == 8 || drawInfo.getImgType() == 2) {
                        List<PointInfo> pointInfos2 = drawInfo.getPointInfos();
                        if (pointInfos2 != null && pointInfos2.size() >= 2) {
                            Paint paint2 = new Paint();
                            paint2.setAlpha(0);
                            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                            PointInfo pointInfo = pointInfos2.get(0);
                            PointInfo pointInfo2 = pointInfos2.get(1);
                            canvas.drawRect(pointInfo.getX(), pointInfo.getY(), pointInfo2.getX(), pointInfo2.getY(), paint2);
                        }
                    } else if (drawInfo.getImgType() == 10) {
                        Paint paint3 = new Paint();
                        paint3.setColor(drawInfo.getPaintColor());
                        paint3.setStyle(Paint.Style.FILL);
                        List<PointInfo> pointInfos3 = drawInfo.getPointInfos();
                        if (!(pointInfos3 == null || pointInfos3.size() == 0)) {
                            PointInfo pointInfo3 = pointInfos3.get(0);
                            PointInfo pointInfo4 = pointInfos3.get(1);
                            canvas.drawRect(pointInfo3.getX(), pointInfo3.getY(), pointInfo4.getX(), pointInfo4.getY(), paint3);
                        }
                    } else if (drawInfo.getImgType() == 4) {
                        List<PointInfo> pointInfos4 = drawInfo.getPointInfos();
                        if (pointInfos4 != null && pointInfos4.size() >= 4) {
                            PointInfo pointInfo5 = pointInfos4.get(0);
                            PointInfo pointInfo6 = pointInfos4.get(1);
                            PointInfo pointInfo7 = pointInfos4.get(2);
                            PointInfo pointInfo8 = pointInfos4.get(3);
                            canvas.drawRect(pointInfo7.getX(), pointInfo7.getY(), pointInfo8.getX(), pointInfo8.getY(), paint);
                            canvas.drawRect(pointInfo5.getX(), pointInfo5.getY(), pointInfo6.getX(), pointInfo6.getY(), paint);
                            if (!(drawInfo.getImgData() == null || (imgFromFile = drawInfo.getImgFromFile()) == null)) {
                                canvas.drawBitmap(imgFromFile, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                                if (!imgFromFile.isRecycled()) {
                                    imgFromFile.recycle();
                                }
                                invalidate();
                            }
                        }
                    } else if (drawInfo.getImgType() == 1 && (pointInfos = drawInfo.getPointInfos()) != null && pointInfos.size() >= 2) {
                        PointInfo pointInfo9 = pointInfos.get(0);
                        PointInfo pointInfo10 = pointInfos.get(1);
                        canvas.drawRect(pointInfo9.getX(), pointInfo9.getY(), pointInfo10.getX(), pointInfo10.getY(), paint);
                        if (!(drawInfo.getImgData() == null || (imgFromFile2 = drawInfo.getImgFromFile()) == null)) {
                            canvas.drawBitmap(imgFromFile2, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                            if (!imgFromFile2.isRecycled()) {
                                imgFromFile2.recycle();
                            }
                            invalidate();
                        }
                    }
                } else {
                    Bitmap imgFromFile6 = drawInfo.getImgFromFile();
                    if (imgFromFile6 != null) {
                        canvas.drawBitmap(imgFromFile6, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                        if (!imgFromFile6.isRecycled()) {
                            imgFromFile6.recycle();
                        }
                    }
                }
            } else {
                Bitmap imgFromFile7 = drawInfo.getImgFromFile();
                if (imgFromFile7 != null) {
                    canvas.drawBitmap(imgFromFile7, drawInfo.getImgX(), drawInfo.getImgY(), this.layerPaint);
                }
                if (imgFromFile7 != null && !imgFromFile7.isRecycled()) {
                    imgFromFile7.recycle();
                }
            }
        }
    }

    public void E0(List<LayerInfo> list) {
        this.e0 = list;
        if (list != null && list.size() > 0 && list.get(0).getVersionCode() < 1102) {
            Iterator<LayerInfo> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LayerInfo next = it.next();
                if (!next.isHide()) {
                    next.setSelected(true);
                    break;
                }
            }
        }
        H0();
    }

    public void F0(List<DrawInfo> list, List<DrawInfo> list2, int i2, Bitmap bitmap) {
        LayerInfo layerInfo = new LayerInfo(false, bitmap, list, new ArrayList(), list2, i2, true);
        if (this.e0 == null) {
            this.e0 = new ArrayList();
        }
        this.e0.clear();
        this.e0.add(layerInfo);
        H0();
        R0();
    }

    public void G0() {
        List<PointInfo> pointInfos;
        if (!m0()) {
            ViewUtils.g(R.string.empty_visible_layer_tip);
            return;
        }
        List<DrawInfo> cancelDrawInfos = this.d0.getCancelDrawInfos();
        if (cancelDrawInfos != null && cancelDrawInfos.size() >= 1) {
            try {
                MainCanvasOP mainActivity = getMainActivity();
                if (mainActivity != null) {
                    Bitmap createBitmap = Bitmap.createBitmap(mainActivity.g(), mainActivity.e(), Bitmap.Config.ARGB_8888);
                    this.d0.setBitmap(createBitmap);
                    if (createBitmap == null) {
                        ViewUtils.g(R.string.operation_failed_try_again);
                        return;
                    }
                    this.mCanvas.setBitmap(createBitmap);
                    if (cancelDrawInfos.size() > 0) {
                        J(cancelDrawInfos.get(cancelDrawInfos.size() - 1));
                        List<DrawInfo> saveDrawInfos = this.d0.getSaveDrawInfos();
                        DrawInfo drawInfo = saveDrawInfos.get(saveDrawInfos.size() - 1);
                        if (drawInfo.getMode() == 0 && drawInfo.getShapeType() == 1 && this.g != null && (pointInfos = drawInfo.getPointInfos()) != null && pointInfos.size() > 0) {
                            if (pointInfos.size() == 1) {
                                this.g.pointX = pointInfos.get(0).getX();
                                this.g.pointY = pointInfos.get(0).getY();
                            } else if (pointInfos.size() == 2) {
                                this.g.pointX = pointInfos.get(1).getX();
                                this.g.pointY = pointInfos.get(1).getY();
                            }
                        }
                        cancelDrawInfos.remove(cancelDrawInfos.size() - 1);
                        I0();
                        R0();
                    }
                }
            } catch (Error e2) {
                e2.printStackTrace();
                ViewUtils.g(R.string.operation_failed_try_again);
            } catch (Exception e3) {
                e3.printStackTrace();
                ViewUtils.g(R.string.operation_failed_try_again);
            }
        }
    }

    public void H0() {
        List<LayerInfo> list = this.e0;
        if (list != null) {
            this.d0 = null;
            this.mCanvas = null;
            if (list.size() > 0) {
                for (LayerInfo layerInfo : this.e0) {
                    if (layerInfo.isSelected()) {
                        this.d0 = layerInfo;
                    }
                }
            }
            if (this.d0 != null) {
                this.mCanvas = new Canvas(this.d0.getBitmap());
            }
            invalidate();
        }
    }

    @SuppressLint({"CheckResult"})
    public void J0(String str, int i2, boolean z, OnSavePhotoListener onSavePhotoListener) {
        try {
            MainCanvasOP mainActivity = getMainActivity();
            if (mainActivity != null) {
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                Bitmap bitmap = null;
                try {
                    bitmap = Bitmap.createBitmap(mainActivity.g(), mainActivity.e(), Bitmap.Config.ARGB_8888);
                } catch (Error e2) {
                    ViewUtils.g(R.string.operation_failed_try_again);
                    e2.printStackTrace();
                } catch (Exception e3) {
                    ViewUtils.g(R.string.operation_failed_try_again);
                    e3.printStackTrace();
                }
                if (bitmap == null) {
                    ViewUtils.g(R.string.operation_failed_try_again);
                    return;
                }
                draw(new Canvas(bitmap));
                Observable.h(bitmap).i(new Function(file, str, mainActivity) { // from class: b.g

                    /* renamed from: b  reason: collision with root package name */
                    public final /* synthetic */ File f122b;

                    /* renamed from: c  reason: collision with root package name */
                    public final /* synthetic */ String f123c;

                    /* renamed from: d  reason: collision with root package name */
                    public final /* synthetic */ MainCanvasOP f124d;

                    {
                        this.f122b = r2;
                        this.f123c = r3;
                        this.f124d = r4;
                    }

                    @Override // io.reactivex.functions.Function
                    public final Object apply(Object obj) {
                        return DrawView.b(DrawView.this, this.f122b, this.f123c, this.f124d, (Bitmap) obj);
                    }
                }).s(Schedulers.b()).l(AndroidSchedulers.a()).p(new Consumer(onSavePhotoListener, z, str, i2) { // from class: b.e

                    /* renamed from: b  reason: collision with root package name */
                    public final /* synthetic */ DrawView.OnSavePhotoListener f114b;

                    /* renamed from: c  reason: collision with root package name */
                    public final /* synthetic */ boolean f115c;

                    /* renamed from: d  reason: collision with root package name */
                    public final /* synthetic */ String f116d;

                    /* renamed from: e  reason: collision with root package name */
                    public final /* synthetic */ int f117e;

                    {
                        this.f114b = r2;
                        this.f115c = r3;
                        this.f116d = r4;
                        this.f117e = r5;
                    }

                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        DrawView.d(DrawView.this, this.f114b, this.f115c, this.f116d, this.f117e, (Boolean) obj);
                    }
                }, new Consumer() { // from class: b.b
                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        DrawView.f(DrawView.OnSavePhotoListener.this, (Throwable) obj);
                    }
                });
            }
        } catch (Exception unused) {
            ViewUtils.g(R.string.save_file_error);
        }
    }

    public void K() {
        if (this.e0 != null) {
            this.e0.add(new LayerInfo(false, Bitmap.createBitmap(getDrawWidth(), getDrawHeight(), Bitmap.Config.ARGB_8888), new ArrayList(), new LinkedList(), new ArrayList(), -1, false));
            H0();
        }
    }

    public void K0() {
        this.mShapeValue = getShape();
        this.mPenSize = getPenSize();
    }

    public void M() {
        MainCanvasOP mainActivity = getMainActivity();
        if (mainActivity != null && mainActivity.g() > 0 && mainActivity.e() > 0) {
            List<PointInfo> list = this.P;
            if (list != null && list.size() > 0) {
                this.P.clear();
            }
            List<DrawPointInfo> list2 = this.R;
            if (list2 != null && list2.size() > 0) {
                this.R.clear();
            }
            List<LayerInfo> list3 = this.e0;
            if (list3 != null && list3.size() > 0) {
                this.e0.clear();
            }
            if (this.e0 == null) {
                this.e0 = new ArrayList(10);
            }
            this.e0.add(new LayerInfo(false, Bitmap.createBitmap(mainActivity.g(), mainActivity.e(), Bitmap.Config.ARGB_8888), new ArrayList(), new LinkedList(), new ArrayList(), -1, true));
            H0();
            this.f277l = true;
            P();
            invalidate();
        }
    }

    public void O() {
        List<Path> list;
        List<Path> list2;
        if (!(this.t0 == null || this.mCanvas == null || (list2 = this.o) == null || this.paintList == null)) {
            if (list2.size() == this.paintList.size()) {
                for (int i2 = 0; i2 < this.o.size(); i2++) {
                    this.mCanvas.drawPath(this.o.get(i2), this.paintList.get(i2));
                }
                invalidate();
            }
            J(this.t0);
        }
        if (!(this.y0 == 0 || this.u0 == null || this.mCanvas == null || (list = this.pathList) == null || this.s == null)) {
            if (list.size() == this.s.size()) {
                for (int i3 = 0; i3 < this.pathList.size(); i3++) {
                    this.mCanvas.drawPath(this.pathList.get(i3), this.s.get(i3));
                }
                invalidate();
            }
            J(this.u0);
        }
        this.q0 = false;
        this.t0 = null;
        this.u0 = null;
        this.o = null;
        this.paintList = null;
        this.pathList = null;
        this.s = null;
        this.p0 = -1.0f;
        this.n0 = -1.0f;
        this.o0 = -1.0f;
        this.m0 = -1.0f;
        invalidate();
    }

    public void S0() {
        MainCanvasOP mainActivity;
        if (!m0()) {
            ViewUtils.g(R.string.empty_visible_layer_tip);
        } else if (this.q0) {
            P();
        } else {
            Bitmap bitmap = null;
            try {
                mainActivity = getMainActivity();
            } catch (Error e2) {
                e2.printStackTrace();
                ViewUtils.g(R.string.operation_failed_try_again);
                return;
            } catch (Exception e3) {
                e3.printStackTrace();
                ViewUtils.g(R.string.operation_failed_try_again);
            }
            if (mainActivity != null) {
                bitmap = Bitmap.createBitmap(mainActivity.g(), mainActivity.e(), Bitmap.Config.ARGB_8888);
                this.d0.setBitmap(bitmap);
                if (bitmap == null) {
                    ViewUtils.g(R.string.operation_failed_try_again);
                    return;
                }
                Canvas canvas = this.mCanvas;
                if (canvas != null) {
                    canvas.setBitmap(bitmap);
                }
                List<DrawInfo> saveDrawInfos = this.d0.getSaveDrawInfos();
                List<DrawInfo> cancelDrawInfos = this.d0.getCancelDrawInfos();
                List<DrawInfo> fullImageDrawInfos = this.d0.getFullImageDrawInfos();
                int lastFullImgIndex = this.d0.getLastFullImgIndex();
                if (saveDrawInfos != null && saveDrawInfos.size() > 0) {
                    cancelDrawInfos.add(saveDrawInfos.get(saveDrawInfos.size() - 1));
                    saveDrawInfos.remove(saveDrawInfos.size() - 1);
                    fullImageDrawInfos.remove(saveDrawInfos.get(saveDrawInfos.size() - 1));
                    if (lastFullImgIndex == saveDrawInfos.size() - 1 && fullImageDrawInfos.size() > 0) {
                        this.d0.setLastFullImgIndex(saveDrawInfos.indexOf(fullImageDrawInfos.get(fullImageDrawInfos.size() - 1)));
                    }
                    I0();
                    R0();
                }
            }
        }
    }

    public void Y(List<PastePhotoSaveInfo> list, OnDrawPastePhotoFinishedListener onDrawPastePhotoFinishedListener) {
        if (this.mCanvas == null) {
            invalidate();
        } else if (this.d0.getSaveDrawInfos() != null) {
            N0();
            this.c0 = Observable.h(list).i(new a()).s(Schedulers.b()).l(AndroidSchedulers.a()).p(new n(onDrawPastePhotoFinishedListener), new o(onDrawPastePhotoFinishedListener));
        } else {
            invalidate();
        }
    }

    public void c0(Bitmap bitmap, int i2, int i3) {
        Canvas canvas = this.mCanvas;
        if (canvas != null) {
            canvas.drawBitmap(bitmap, (float) i2, (float) i3, this.layerPaint);
            if (this.d0.getSaveDrawInfos() != null) {
                this.c0 = Observable.h(1).i(new d()).s(Schedulers.b()).l(AndroidSchedulers.a()).p(new b(), new c());
            } else {
                invalidate();
            }
        }
    }

    public List<Paint> getDrawPaintList(DrawInfo drawInfo) {
        ArrayList arrayList = new ArrayList();
        Paint paint = new Paint();
        arrayList.add(paint);
        paint.setAntiAlias(true);
        paint.setColor(drawInfo.getPaintColor());
        paint.setStrokeWidth(drawInfo.getPaintSize());
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(null);
        paint.setMaskFilter(null);
        paint.setShader(null);
        if (drawInfo.isFillMode()) {
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        if (drawInfo.getMode() == 1) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            paint.setAlpha(0);
        }
        String text = drawInfo.getText();
        if (!TextUtils.isEmpty(text)) {
            try {
                ExtendDrawInfo extendDrawInfo =   new Gson().fromJson(text,   ExtendDrawInfo.class);
                if (PaintAppUtils.m(drawInfo.getShapeType())) {
                    paint.setPathEffect(null);
                    Paint paint2 = new Paint(paint);
                    paint2.setPathEffect(new DashPathEffect(new float[]{drawInfo.getPaintSize(), drawInfo.getPaintSize() * 2.0f}, 0.0f));
                    arrayList.add(paint2);
                } else {
                    int lineType = extendDrawInfo.getLineType();
                    if (lineType == 1) {
                        paint.setPathEffect(null);
                    } else if (lineType == 2) {
                        paint.setPathEffect(new DashPathEffect(new float[]{drawInfo.getPaintSize(), drawInfo.getPaintSize() * 2.0f}, 0.0f));
                    }
                }
                int penType = extendDrawInfo.getPenType();
                paint.setShader(getShaderByPenType(penType, drawInfo.getCustomBrushImgPath(), paint));
                paint.setMaskFilter(getMaskFilterByPenType(penType, drawInfo.getPaintSize()));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (drawInfo.isLightEffect()) {
            paint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.SOLID));
        }
        return arrayList;
    }

    public int getColor() {
        return this.B;
    }

    public int getCurrentShape() {
        return this.mShapeValue;
    }

    public int getCutFillColor() {
        return this.f268a;
    }

    public int getEraseSize() {
        return this.M;
    }

    public int getFillColor() {
        return this.C;
    }

    public int getLastMode() {
        return this.mLastMode;
    }

    public List<LayerInfo> getLayerInfos() {
        return this.e0;
    }

    public MainCanvasOP getMainActivity() {
        Context context = this.context;
        if (context instanceof PaintMainActivity) {
            return (PaintMainActivity) context;
        }
        if (context instanceof PaperPaintMainActivity2) {
            return (PaperPaintMainActivity2) context;
        }
        return null;
    }

    public int getPaintMode() {
        return this.mPaintMode;
    }

    public int getPaintTextColor() {
        return this.paintTextColor;
    }

    public int getPaintTextColorProgress() {
        return this.F;
    }

    public List<Paint> getPaints() {
        return this.paintList;
    }

    public float getPenSize() {
        return this.D;
    }

    public int getShape() {
        return this.f271d;
    }

    public float[] getSymmertyPoints() {
        return this.z0;
    }

    public int getSymmertyType() {
        return this.y0;
    }

    public List<Path> getDrawPathList(DrawInfo drawInfo) {
        List<PointInfo> pointInfos = drawInfo.getPointInfos();
        if (pointInfos == null || pointInfos.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Path path = new Path();
        arrayList.add(path);
        int shapeType = drawInfo.getShapeType();
        int mode = drawInfo.getMode();
        int i2 = 0;
        if (mode == 0) {
            if (shapeType == 7) {
                path.moveTo(pointInfos.get(0).getX(), pointInfos.get(0).getY());
                while (i2 < pointInfos.size()) {
                    PointInfo pointInfo = pointInfos.get(i2);
                    PointInfo pointInfo2 = pointInfos.get(i2 + 1);
                    path.quadTo(pointInfo.getX(), pointInfo.getY(), pointInfo2.getX(), pointInfo2.getY());
                    i2 += 2;
                }
            } else if (shapeType == 0) {
                PointInfo pointInfo3 = pointInfos.get(0);
                PointInfo pointInfo4 = pointInfos.get(1);
                path.moveTo(pointInfo3.getX(), pointInfo3.getY());
                path.lineTo(pointInfo4.getX(), pointInfo4.getY());
            } else if (shapeType == 28 || shapeType == 29 || shapeType == 30 || shapeType == 31) {
                PointInfo pointInfo5 = pointInfos.get(0);
                PointInfo pointInfo6 = pointInfos.get(1);
                DrawUtils.a(shapeType, path, pointInfo5.getX(), pointInfo5.getY(), pointInfo6.getX(), pointInfo6.getY());
            } else if (shapeType == 1) {
                if (pointInfos.size() == 1) {
                    PointInfo pointInfo7 = pointInfos.get(0);
                    path.addCircle(pointInfo7.getX(), pointInfo7.getY(), 0.5f, Path.Direction.CW);
                } else if (pointInfos.size() == 2) {
                    PointInfo pointInfo8 = pointInfos.get(0);
                    PointInfo pointInfo9 = pointInfos.get(1);
                    path.moveTo(pointInfo8.getX(), pointInfo8.getY());
                    path.lineTo(pointInfo9.getX(), pointInfo9.getY());
                }
            } else if (shapeType == 2 || shapeType == 3) {
                PointInfo pointInfo10 = pointInfos.get(0);
                PointInfo pointInfo11 = pointInfos.get(1);
                path.addRect(pointInfo10.getX(), pointInfo10.getY(), pointInfo11.getX(), pointInfo11.getY(), Path.Direction.CW);
            } else if (shapeType == 20) {
                PointInfo pointInfo12 = pointInfos.get(0);
                PointInfo pointInfo13 = pointInfos.get(1);
                path.moveTo(pointInfo12.getX(), pointInfo12.getY());
                path.lineTo(pointInfo13.getX(), pointInfo13.getY());
                float x = pointInfo12.getX();
                path.lineTo(x, (pointInfo13.getY() * 2.0f) - pointInfo12.getY());
                path.lineTo((x * 2.0f) - pointInfo13.getX(), pointInfo13.getY());
                path.close();
            } else if (shapeType == 17) {
                PointInfo pointInfo14 = pointInfos.get(0);
                PointInfo pointInfo15 = pointInfos.get(1);
                b0(pointInfo14.getX(), pointInfo14.getY(), pointInfo15.getX(), pointInfo15.getY(), path);
            } else if (shapeType == 13 || shapeType == 4 || shapeType == 8 || shapeType == 14 || shapeType == 9 || shapeType == 15 || shapeType == 21) {
                PointInfo pointInfo16 = pointInfos.get(0);
                path.moveTo(pointInfo16.getX(), pointInfo16.getY());
                for (int i3 = 1; i3 < pointInfos.size(); i3++) {
                    PointInfo pointInfo17 = pointInfos.get(i3);
                    path.lineTo(pointInfo17.getX(), pointInfo17.getY());
                }
                path.close();
            } else if (shapeType == 18) {
                PointInfo pointInfo18 = pointInfos.get(0);
                PointInfo pointInfo19 = pointInfos.get(1);
                Z(pointInfo18.getX(), pointInfo18.getY(), pointInfo19.getX(), pointInfo19.getY(), drawInfo.isFillMode(), path);
            } else if (shapeType == 19) {
                PointInfo pointInfo20 = pointInfos.get(0);
                PointInfo pointInfo21 = pointInfos.get(1);
                W(pointInfo20.getX(), pointInfo20.getY(), pointInfo21.getX(), pointInfo21.getY(), drawInfo.isFillMode(), path);
            } else if (shapeType == 5) {
                PointInfo pointInfo22 = pointInfos.get(0);
                PointInfo pointInfo23 = pointInfos.get(1);
                path.addOval(new RectF(pointInfo22.getX(), pointInfo22.getY(), pointInfo23.getX(), pointInfo23.getY()), Path.Direction.CW);
            } else if (shapeType == 6) {
                PointInfo pointInfo24 = pointInfos.get(0);
                PointInfo pointInfo25 = pointInfos.get(1);
                path.addCircle(pointInfo24.getX(), pointInfo24.getY(), (float) Math.sqrt(Math.pow((double) (pointInfo24.getX() - pointInfo25.getX()), 2.0d) + Math.pow((double) (pointInfo24.getY() - pointInfo25.getY()), 2.0d)), Path.Direction.CW);
            } else if (shapeType == 10) {
                PointInfo pointInfo26 = pointInfos.get(0);
                path.moveTo(pointInfo26.getX(), pointInfo26.getY());
                path.lineTo(pointInfos.get(1).getX(), pointInfos.get(1).getY());
                path.lineTo(pointInfos.get(2).getX(), pointInfos.get(2).getY());
                path.moveTo(pointInfos.get(3).getX(), pointInfos.get(3).getY());
                path.lineTo(pointInfos.get(4).getX(), pointInfos.get(4).getY());
            } else if (shapeType == 12) {
                PointInfo pointInfo27 = pointInfos.get(0);
                PointInfo pointInfo28 = pointInfos.get(1);
                U(pointInfo28.getX(), pointInfo28.getY(), pointInfo27.getX(), pointInfo27.getY(), path);
                path.moveTo(pointInfo28.getX(), pointInfo28.getY());
                path.lineTo(pointInfo27.getX(), pointInfo27.getY());
            } else if (shapeType == 11) {
                PointInfo pointInfo29 = pointInfos.get(0);
                PointInfo pointInfo30 = pointInfos.get(1);
                U(pointInfo30.getX(), pointInfo30.getY(), pointInfo29.getX(), pointInfo29.getY(), path);
            } else if (shapeType == 16) {
                PointInfo pointInfo31 = pointInfos.get(0);
                PointInfo pointInfo32 = pointInfos.get(1);
                V(pointInfo31.getX(), pointInfo31.getY(), pointInfo32.getX(), pointInfo32.getY(), path);
            } else if (shapeType == 22) {
                PointInfo pointInfo33 = pointInfos.get(0);
                PointInfo pointInfo34 = pointInfos.get(1);
                Path path2 = new Path();
                arrayList.add(path2);
                S(pointInfo33.getX(), pointInfo33.getY(), pointInfo34.getX(), pointInfo34.getY(), path, path2);
            } else if (shapeType == 23) {
                PointInfo pointInfo35 = pointInfos.get(0);
                PointInfo pointInfo36 = pointInfos.get(1);
                Path path3 = new Path();
                arrayList.add(path3);
                R(pointInfo35.getX(), pointInfo35.getY(), pointInfo36.getX(), pointInfo36.getY(), path, path3);
            } else if (shapeType == 24) {
                PointInfo pointInfo37 = pointInfos.get(0);
                PointInfo pointInfo38 = pointInfos.get(1);
                Path path4 = new Path();
                arrayList.add(path4);
                T(pointInfo37.getX(), pointInfo37.getY(), pointInfo38.getX(), pointInfo38.getY(), path, path4);
            } else if (shapeType == 25) {
                PointInfo pointInfo39 = pointInfos.get(0);
                PointInfo pointInfo40 = pointInfos.get(1);
                Path path5 = new Path();
                arrayList.add(path5);
                Q(pointInfo39.getX(), pointInfo39.getY(), pointInfo40.getX(), pointInfo40.getY(), path, path5);
            } else if (shapeType == 26) {
                PointInfo pointInfo41 = pointInfos.get(0);
                PointInfo pointInfo42 = pointInfos.get(1);
                Path path6 = new Path();
                arrayList.add(path6);
                a0(pointInfo41.getX(), pointInfo41.getY(), pointInfo42.getX(), pointInfo42.getY(), path, path6);
            }
        } else if (mode == 1) {
            path.moveTo(pointInfos.get(0).getX(), pointInfos.get(0).getY());
            while (i2 < pointInfos.size()) {
                PointInfo pointInfo43 = pointInfos.get(i2);
                int i4 = i2 + 1;
                if (i4 < pointInfos.size()) {
                    PointInfo pointInfo44 = pointInfos.get(i4);
                    path.quadTo(pointInfo43.getX(), pointInfo43.getY(), pointInfo44.getX(), pointInfo44.getY());
                }
                i2 += 2;
            }
        }
        return arrayList;
    }

    public MaskFilter getMaskFilterByPenType(int i2, float f2) {
        if (i2 == 3) { // 喷笔1
            return new BlurMaskFilter(f2 * 0.2f, BlurMaskFilter.Blur.NORMAL);
        }
        if (i2 == 4) { // 彩虹笔
            return new BlurMaskFilter(f2 * 0.5f, BlurMaskFilter.Blur.OUTER);
        }
        if (i2 == 6) { // 喷笔2
            return new BlurMaskFilter(f2, BlurMaskFilter.Blur.NORMAL);
        }
        return null;
    }

    public Shader getShaderByPenType(int i2, String str, Paint paint) {
        paint.setStrokeCap(Paint.Cap.ROUND);
        if (i2 == 1) { // 圆珠笔
            return null;
        }
        if (i2 == 7) { // 自定义背景笔
            paint.setStrokeCap(Paint.Cap.SQUARE);
            return null;
        } else if (i2 == 2) { // 铅笔
            Bitmap decodeResource = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_pencil);
            if (decodeResource == null) {
                return null;
            }
            Canvas canvas = new Canvas();
            Bitmap createBitmap = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), Bitmap.Config.ARGB_8888);
            createBitmap.eraseColor(Color.rgb(Color.red(paint.getColor()), Color.green(paint.getColor()), Color.blue(paint.getColor())));
            canvas.setBitmap(createBitmap);
            Paint paint2 = new Paint();
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(decodeResource, 0.0f, 0.0f, paint2);
            Shader.TileMode tileMode = Shader.TileMode.REPEAT;
            return new BitmapShader(createBitmap, tileMode, tileMode);
        } else if (i2 == 8) { // 本地图片
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            Shader.TileMode tileMode2 = Shader.TileMode.REPEAT;
            return new BitmapShader(decodeFile, tileMode2, tileMode2);
        } else if (i2 != 5) {  // 其它
            return null;
        } else { // 宽笔
            Bitmap createBitmap2 = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(createBitmap2);
            Paint paint3 = new Paint();
            paint3.setAntiAlias(true);
            paint3.setDither(true);
            paint3.setStrokeWidth(48.0f);
            paint3.setStyle(Paint.Style.STROKE);
            paint3.setShader(new LinearGradient(0.0f, 24.0f, 48.0f, 24.0f, new int[]{Color.parseColor("#FF0000"), Color.parseColor("#FF7F00"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00"), Color.parseColor("#00FFFF"), Color.parseColor("#0000FF"), Color.parseColor("#8B00FF")}, (float[]) null, Shader.TileMode.CLAMP));
            canvas2.drawLine(0.0f, 24.0f, 48.0f, 24.0f, paint3);
            Shader.TileMode tileMode3 = Shader.TileMode.REPEAT;
            return new BitmapShader(createBitmap2, tileMode3, tileMode3);
        }
    }

    public boolean m0() {
        return this.d0 != null;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Disposable disposable = this.c0;
        if (disposable != null && !disposable.k()) {
            this.c0.dispose();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        Paint paint;
        Path path;
        List<Path> list;
        List<Paint> list2;
        List<Paint> list3;
        int i2;
        super.onDraw(canvas);
        drawLayers(canvas);
        List<Path> list4 = this.o;
        if (list4 != null && list4.size() > 0 && (list3 = this.paintList) != null && list3.size() > 0 && this.o.size() == this.paintList.size() && (((i2 = this.mPaintMode) == 0 || i2 == 2) && this.f271d != 27)) {
            for (int i3 = 0; i3 < this.o.size(); i3++) {
                canvas.drawPath(this.o.get(i3), this.paintList.get(i3));
            }
        }
        if (getSymmertyType() != 0 && (list = this.pathList) != null && list.size() > 0 && (list2 = this.s) != null && list2.size() > 0 && this.pathList.size() == this.s.size() && this.mPaintMode == 0 && this.f271d != 27) {
            for (int i4 = 0; i4 < this.pathList.size(); i4++) {
                canvas.drawPath(this.pathList.get(i4), this.s.get(i4));
            }
        }
        if (!(!this.q0 || (paint = this.u) == null || (path = this.t) == null || this.r0 == null || this.t0 == null || this.s0 == null)) {
            canvas.drawPath(path, paint);
            float elementTop = this.t0.getElementTop() - ((float) PixeUtils.a(this.context, 10.0f));
            canvas.drawBitmap(this.r0, this.t0.getElementLeft() - ((float) PixeUtils.a(this.context, 10.0f)), elementTop, this.u);
            canvas.drawBitmap(this.s0, this.t0.getElementRight() - ((float) PixeUtils.a(this.context, 10.0f)), elementTop, this.u);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00ab, code lost:
        if (r9 != 3) goto L_0x022f;
     */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r18) {
        /*
        // Method dump skipped, instructions count: 560
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.fjnu.edu.paint.engine.DrawView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            this.k0 = SharedPreferenceService.h();
        }
    }

    public void q0() {
        if (this.layerPaint == null) {
            Paint paint = new Paint();
            this.layerPaint = paint;
            paint.setAntiAlias(true);
            this.layerPaint.setDither(true);
            this.layerPaint.setFilterBitmap(true);
        }
        MainCanvasOP mainActivity = getMainActivity();
        if (mainActivity != null) {
            List<LayerInfo> list = this.e0;
            if ((list == null || list.size() == 0) && mainActivity.g() > 0 && mainActivity.e() > 0) {
                List<LayerInfo> list2 = this.e0;
                if (list2 != null) {
                    list2.clear();
                }
                this.e0 = null;
                this.d0 = null;
                this.mCanvas = null;
                this.e0 = new ArrayList(10);
                this.e0.add(new LayerInfo(false, Bitmap.createBitmap(Bitmap.createBitmap(getDrawWidth(), getDrawHeight(), Bitmap.Config.ARGB_8888)), new ArrayList(), new LinkedList(), new ArrayList(), -1, true));
                H0();
            }
            List<Paint> list3 = this.paintList;
            if (list3 != null) {
                list3.clear();
            }
            this.paintList = null;
            this.paintList = new ArrayList();
            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeJoin(Paint.Join.ROUND);
            paint2.setStrokeCap(Paint.Cap.ROUND);
            paint2.setStrokeWidth(this.D);
            paint2.setColor(this.B);
            paint2.setMaskFilter(null);
            paint2.setPathEffect(null);
            paint2.setShader(null);
            this.paintList.add(paint2);
            List<Paint> list4 = this.s;
            if (list4 != null) {
                list4.clear();
            }
            this.s = null;
            this.s = new ArrayList();
            Paint paint3 = new Paint(paint2);
            this.s.add(paint3);
            if (this.mPaintMode == 1) {
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                paint2.setAlpha(0);
                paint2.setStrokeWidth((float) this.M);
            }
            if (this.mPaintMode == 2) {
                paint2.setColor(ContextCompat.getColor(getContext(), R.color.half_gray_4c));
                paint2.setStrokeWidth(10.0f);
            }
            if (this.mPaintMode == 0) {
                Paint[] paintArr = {paint2, paint3};
                for (int i2 = 0; i2 < 2; i2++) {
                    Paint paint4 = paintArr[i2];
                    int i3 = this.f271d;
                    if (i3 == 1 || i3 == 0 || i3 == 10 || i3 == 17 || PaintAppUtils.m(i3)) {
                        paint4.setStyle(Paint.Style.STROKE);
                    } else if (GlobalValue.f254a) {
                        paint4.setStyle(Paint.Style.FILL);
                    } else {
                        paint4.setStyle(Paint.Style.STROKE);
                    }
                    int i4 = BaseGlobalValue.currentLineType;
                    if (i4 == 1) {
                        paint4.setPathEffect(null);
                    } else if (i4 == 2) {
                        if (PaintAppUtils.m(this.f271d)) {
                            paint4.setPathEffect(null);
                        } else {
                            float f2 = this.D;
                            paint4.setPathEffect(new DashPathEffect(new float[]{f2, f2 * 2.0f}, 0.0f));
                        }
                    }
                    paint4.setShader(getShaderByPenType(BaseGlobalValue.currentPenType, BaseGlobalValue.u, paint4));
                    paint4.setMaskFilter(getMaskFilterByPenType(BaseGlobalValue.currentPenType, this.D));
                    if (BaseGlobalValue.g) {
                        paint4.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.SOLID));
                    }
                }
            }
        }
    }

    public boolean s0() {
        List<LayerInfo> list = this.e0;
        boolean z = false;
        if (!(list == null || list.size() == 0)) {
            for (LayerInfo layerInfo : this.e0) {
                if (layerInfo.isSelected() && !layerInfo.isHide()) {
                    z = true;
                }
            }
        }
        return z;
    }

    public void setAreaSelectType(int i2) {
        this.T = i2;
    }

    public void setColor(int i2) {
        this.B = i2;
    }

    public void setCutFillColor(int i2) {
        this.f268a = i2;
    }

    public void setEraseSize(int i2) {
        this.M = i2;
    }

    public void setFillColor(int i2) {
        this.C = i2;
        this.mPaintMode = 4;
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public void setLastMode(int lastMode) {
        this.mLastMode = lastMode;
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
    }

    public void setPaint(List<Paint> list) {
        this.paintList = list;
    }

    public void setPaintMode(int paintMode) {
        MainCanvasOP mainActivity = getMainActivity();
        if (mainActivity != null) {
            mainActivity.findViewById(R.id.zoom_seekbar).setVisibility(8);
            mainActivity.findViewById(R.id.roat_seekbar).setVisibility(8);
        }
        this.mPaintMode = paintMode;
        if (paintMode == 0) {
            setShape(this.mShapeValue);
            setPenSize(this.mPenSize);
        } else if (paintMode == 1) {
            if (!this.m) {
                this.mShapeValue = getShape();
                this.mPenSize = getPenSize();
                this.m = true;
            }
            setShape(7);
        } else if (paintMode == 2) {
            if (!this.m) {
                this.mShapeValue = getShape();
                this.mPenSize = getPenSize();
                this.m = true;
            }
            setShape(2);
            this.T = -1;
        }
    }

    public void setPaintTextColor(int paintTextColor) {
        this.paintTextColor = paintTextColor;
        Paint paint = this.paint;
        if (paint != null) {
            paint.setColor(paintTextColor);
        }
    }

    public void setPaintTextColorProgress(int i2) {
        this.F = i2;
    }

    public void setPaintTextSize(float paintTextSize) {
        Paint paint = this.paint;
        if (paint != null) {
            paint.setTextSize(paintTextSize);
        }
    }

    public void setPenSize(float penSize) {
        this.D = penSize;
        this.mPenSize = penSize;
    }

    public void setShape(int shape) {
        this.f271d = shape;
    }

    public void setSymmetryPoints(float[] fArr) {
        this.z0 = fArr;
    }

    public void setSymmetryType(int i2) {
        this.y0 = i2;
    }

    public void setTouchable(boolean z) {
        this.N = z;
    }

    public void setUserTouchListener(OnUserTouchListener onUserTouchListener) {
        this.onUserTouchListener = onUserTouchListener;
    }

    public DrawView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawView(Context context) {
        this(context, null);
    }
}
