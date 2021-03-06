package com.bixian365.dzc.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.fragment.bill.BillFragment;
import com.bixian365.dzc.fragment.car.PadCarFragment;
import com.bixian365.dzc.fragment.goods.GoodsListFragment;
import com.bixian365.dzc.fragment.home.HomeFragment;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.download.DownloadOkHttpUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.BadgeView;
import com.lzy.okhttputils.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * ***************************
 * 主结构管理类
 * @author mfx
 * ***************************
 */
public class MainFragmentActivity extends AppCompatActivity {
    public static RadioButton homeRb, goodsRb, billRb,carRb,myRb;
    private RadioGroup radioGroup;
    public FragmentManager fragmentManager;
    public FragmentTransaction transaction;

    public HomeFragment homeFrag;
    public BillFragment billFrag;
    public GoodsListFragment goodsFrag;
//    public CarFragment  carFrag;
    public PadCarFragment PadCarFrag;
    public Fragment lastshowFragment;
    private DisplayMetrics dm;
    public static final int FLAG = 100;//进入判断登录后返回主页 标示
    //    public  static boolean isShow=true;//判断是否主页打开过
    private String gesturePhone;//获取用户登录手机号用于判断是否使用手势密码登录
    public static BadgeView badge1;
    public static int totalCarNum=0;//所有购物车数量
    public static String totalCarPrice="0";//购物车商品总价格
    private String tag;//1 为普通用户，店铺  显示商品及购买 2 为合伙人等角色
    private Activity activity;
    private int displayTag=1;//当前显示tab 保存在未登录点击选项卡位置
    private Intent intentService;//双屏幕显示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        tag = this.getIntent().getStringExtra("tag");
        activity = this;
//        isShow = false;
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        SXUtils.getInstance(this).setSysStatusBar(this);
//        透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        SXUtils.getInstance(this).addActivity(activity);
        EventBus.getDefault().register(this);
        init();
        intentService = new Intent(activity,XHShowService.class);
        startService(intentService);
//        new TimeThread().start();
//        compat(this);
    }

    private static final int INVALID_VAL = -1;
    public static void compat(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.qblue));//activity.getResources().getColor(R.color.blue));
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View statusBarView = new View(activity);
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusBarHeight);
            statusBarView.setBackgroundColor(activity.getResources().getColor(R.color.qblue));
            contentView.addView(statusBarView, lp);
        }
    }
    private void init() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        AppClient.fullWidth = dm.widthPixels;
        AppClient.fullHigh = dm.heightPixels;
        Logs.i("屏幕宽高========", AppClient.fullWidth + "====" + AppClient.fullHigh);
//        FragmentManager childFragmentManager = this.getChildFragmentManager();
        fragmentManager = this.getFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.main_groub_tab);
        radioGroup.setOnCheckedChangeListener(new OnCheckChanged());

        homeRb = (RadioButton) findViewById(R.id.main_tab_home_rb);
        goodsRb = (RadioButton) findViewById(R.id.main_tab_financial_rb);
        billRb = (RadioButton) findViewById(R.id.main_tab_live_rb);
        carRb = (RadioButton) findViewById(R.id.main_tab_car_rb);
        myRb = (RadioButton) findViewById(R.id.main_tab_my_rb);
        carRb.setChecked(true);
    }
    public static MainFragmentActivity instance;

    public static MainFragmentActivity getInstance() {
        if (instance == null)
            instance = new MainFragmentActivity();
        return instance;
    }

    /**
     * 在第一次查询购物车调用
     * 设置购物车数量
     * @param num
     */
    public void setBadgeNum(int num){
        if(num>0){
            badge1.show();
            if(num >99){
                badge1.setText(99+"+");
            }else{
                badge1.setText(num+"");
            }
        }else{
            badge1.hide();
            badge1.setText("0");
        }
        MainFragmentActivity.totalCarNum = num;
    }
    public void addFragmentToStack(Fragment fragment) {
        if (fragmentManager == null)
            fragmentManager = this.getFragmentManager();
        if (transaction == null)
            transaction = fragmentManager.beginTransaction();
        if (lastshowFragment != null)
            transaction.hide(lastshowFragment);
        transaction.show(fragment);
        lastshowFragment = fragment;
        transaction.commit();
    }
    private void setTab(int tag){
        switch (tag){
            case 1:
                homeRb.setChecked(true);
                break;
            case 2:
                goodsRb.setChecked(true);
                break;
            case 3:
                billRb.setChecked(true);
                break;
            case 4:
                carRb.setChecked(true);
                break;
            case 5:myRb.setChecked(true);
                break;
        }
    }
    class OnCheckChanged implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            transaction = fragmentManager.beginTransaction();
            if (checkedId == homeRb.getId()) {
                displayTag = 1;
                if (homeFrag == null) {
                    homeFrag = new HomeFragment();
                    transaction.add(R.id.content, homeFrag);
                }
                addFragmentToStack(homeFrag);
            } else if (billRb.getId() == checkedId) {

                if(!SXUtils.getInstance(activity).IsLogin()){
                    setTab(displayTag);
                    return ;
                }
                displayTag = 3;
                if (billFrag == null) {
                    billFrag = new BillFragment();
                    transaction.add(R.id.content, billFrag);
                }
                addFragmentToStack(billFrag);

            }
//            else if (goodsRb.getId() == checkedId) {
//                displayTag = 2;
//                if (goodsFrag == null) {
//                    goodsFrag = new GoodsListFragment();
//                    transaction.add(R.id.content, goodsFrag);
//                }
//                addFragmentToStack(goodsFrag);
//            }
            else if (myRb.getId() == checkedId) {
                displayTag = 5;
                //根据登陆后获取的用户表示来判断我的界面显示对应布局
//                用户标签，1:后台用户,2:城市采购中心,4:供应商,8:联创中心,16:合伙人,32:摊主店铺,64:消费者,128:供应商司机,256:采购中心司机
                if(AppClient.USERROLETAG.equals("4") ){

                }else if(AppClient.USERROLETAG.equals("16") || AppClient.USERROLETAG.equals("8")){
                }
                else{
                    //if(AppClient.USERROLETAG.equals("64") || AppClient.USERROLETAG.equals("32"))
                }
            }else if(carRb.getId() == checkedId){
                if(!SXUtils.getInstance(activity).IsLogin()){
                    setTab(displayTag);
                    return ;
                }
                displayTag = 4;
//                if (PadCarFrag == null) {
//                    PadCarFrag = new PadCarFragment();
//                    transaction.add(R.id.content,PadCarFrag);
//                }
//                addFragmentToStack(PadCarFrag);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exitSystem(MainFragmentActivity.this);
        }
        return true;
    }
    public long firstTime = 0;
    public void exitSystem(Activity context) {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出
            Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_LONG).show();
            firstTime = secondTime;//更新firstTime
            return;
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
            SXUtils.getInstance(context).finishActivity();
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()==6666){
            progressBar.setProgress(Integer.parseInt(messageEvent.getMessage()));
            progressnumTv.setText(messageEvent.getMessage()+"%");
            if(Integer.parseInt(messageEvent.getMessage()) == 100){
                if(!mustUpdate.equals("1")) {
                    if (proDialog != null) {
                        proDialog.dismiss();
                    }
                }
            }

        }else if(messageEvent.getTag()==AppClient.UPDATEVER){
            String message = messageEvent.getMessage();
//            "responseData":{"forceUpdate":"true","upgradeDesc":"xxx","upgradeUrl":"http://spa.xianhao365.com/..."}
            String content="";
            String updateUrl=null;
            String forceUpdate = null;
            String version = null;
            try {
                JSONObject jsonObject = new JSONObject(message);
                content = jsonObject.getString("upgradeDesc");
                updateUrl = jsonObject.getString("upgradeUrl");
                forceUpdate=jsonObject.getString("forceUpgrade");
                version = jsonObject.getString("softwareVersion");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final  String updateStr = updateUrl;
            mustUpdate=forceUpdate;
            final  boolean  isMustUpdate;
            if(!mustUpdate.equals("1"))  //1 强制更新  0 不强制更新
            {
                isMustUpdate = true;
            }
            else{
                isMustUpdate = false;
            }
            SXUtils.getInstance(activity).UpdateConfrimDialogView(activity,isMustUpdate, "  版本更新\n新版本:"+version+"",content+"", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mustUpdate.equals("1"))
                    {
                        SXUtils.getInstance(activity).tipDialog.dismiss();
                    }
                    UpdateProgressDialog(activity, isMustUpdate, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!mustUpdate.equals("1")) {
                                proDialog.dismiss();
                            }else{
                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(startMain);
                                SXUtils.getInstance(activity).finishActivity();
                                System.exit(0);
                                finish();
                            }
                            OkHttpUtils.getInstance().cancelTag("downapk");
                        }
                    });
                    //文件下载 版本升级
                    DownloadOkHttpUtils.DownFile(activity,updateStr,progressBar,progressnumTv);
//                    SXUtils.getInstance(activity).tipDialog.dismiss();
                    //弹出H5界面更新下载
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = Uri.parse("" + updateStr);
//                    intent.setData(content_url);
//                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    String mustUpdate;
    AlertDialog proDialog;
    ProgressBar  progressBar;
    TextView progressnumTv;
    TextView cancleTv;
    /**
     * 下载apk更新进度条显示框
     * @param context
     * @param cancelable
     * @param onClickListener
     */
    public void UpdateProgressDialog(Context context, boolean cancelable, View.OnClickListener onClickListener) {
        proDialog = new AlertDialog.Builder(context).create();
        proDialog.show();
        proDialog.setCancelable(cancelable);
        proDialog.setCanceledOnTouchOutside(false);
        Window window = proDialog.getWindow();
        window.setContentView(R.layout.progress_update_dialog);
        progressnumTv = (TextView) window.findViewById(R.id.update_progress_num_tv);
        cancleTv = (TextView) window.findViewById(R.id.update_progress_cancel_tv);
        progressBar = (ProgressBar) window.findViewById(R.id.update_progress_v);
        if(!cancelable){
            cancleTv.setText("退出皕鲜");
        }else{
            cancleTv.setText("取消下载");
        }
        cancleTv.setOnClickListener(onClickListener);

    }
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    EventBus.getDefault().post(new MessageEvent(AppClient.PADEVENT00002,""));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}