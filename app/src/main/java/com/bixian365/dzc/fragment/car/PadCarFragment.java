package com.bixian365.dzc.fragment.car;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.scale.uart.IReadDataListener;
import com.android.scale.uart.PrintUtils;
import com.android.scale.uart.UartOptNative;
import com.android.scale.uart.UartReadThread;
import com.android.scale.uart.Utils;
import com.android.scale.uart.ZhilingTest;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.member.LoginNameActivity;
import com.bixian365.dzc.adapter.PadCarGoodsListRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.car.ShoppingCartLinesEntity;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.entity.goodstype.PadGoodsTypeGoodsEntity;
import com.bixian365.dzc.fragment.XHShowService;
import com.bixian365.dzc.fragment.goods.GoodsListFragment;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.download.DownloadOkHttpUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.RequestReqMsgData;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PadCarFragment extends Activity implements IReadDataListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private Activity activity;
    private Handler hand;
    private int typeIndexPage = 0;
    private int goodsIndexPage = 0;
    private int typeBtnPosion = 0;
    private int goodsBtnPosion = 0;
    private PadCarGoodsListRecyclerViewAdapter simpAdapter;
    private List<PadGoodsTypeGoodsEntity> Typelist;
    @BindView(R.id.pad_car_update_number_btn)
    Button updateNumBtn;
    @BindView(R.id.pad_car_total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.pad_car_goodsnumber_tv)
    TextView goodsNumberTv;
    @BindView(R.id.pad_car_now_time_tv)
    TextView nowTimeTv;
    @BindView(R.id.pad_car_goods_manage_btn)
    TextView goodsManageTv;
    @BindView(R.id.pad_car_type1)
    Button typeBtn1;
    @BindView(R.id.pad_car_type2)
    Button typeBtn2;
    @BindView(R.id.pad_car_type3)
    Button typeBtn3;
    @BindView(R.id.pad_car_type4)
    Button typeBtn4;
    @BindView(R.id.pad_car_type5)
    Button typeBtn5;
    @BindView(R.id.pad_car_type6)
    Button typeBtn6;
    @BindView(R.id.pad_car_type7)
    Button typeBtn7;
    @BindView(R.id.pad_car_type8)
    Button typeBtn8;
    @BindView(R.id.pad_car_type9)
    Button typeBtn9;
    @BindView(R.id.pad_car_type10)
    Button typeBtn10;
    @BindView(R.id.pad_car_type_last_setp)
    Button typeLastBtn;
    @BindView(R.id.pad_car_type_next)
    Button typeNextBtn;
    //
    @BindView(R.id.pad_car_goods1)
    LinearLayout goodsBtn1;
    @BindView(R.id.pad_car_goods2)
    LinearLayout goodsBtn2;
    @BindView(R.id.pad_car_goods3)
    LinearLayout goodsBtn3;
    @BindView(R.id.pad_car_goods4)
    LinearLayout goodsBtn4;
    @BindView(R.id.pad_car_goods5)
    LinearLayout goodsBtn5;
    @BindView(R.id.pad_car_goods6)
    LinearLayout goodsBtn6;
    @BindView(R.id.pad_car_goods7)
    LinearLayout goodsBtn7;
    @BindView(R.id.pad_car_goods8)
    LinearLayout goodsBtn8;
    @BindView(R.id.pad_car_goods9)
    LinearLayout goodsBtn9;
    @BindView(R.id.pad_car_goods10)
    LinearLayout goodsBtn10;
    @BindView(R.id.pad_car_goods_last_step)
    Button goodsLastBtn;
    @BindView(R.id.pad_car_goods_next)
    Button goodsNextBtn;

    @BindView(R.id.pad_car_goodsname1)
    TextView goodsNameTv1;
    @BindView(R.id.pad_car_goodsname2)
    TextView goodsNameTv2;
    @BindView(R.id.pad_car_goodsname3)
    TextView goodsNameTv3;
    @BindView(R.id.pad_car_goodsname4)
    TextView goodsNameTv4;
    @BindView(R.id.pad_car_goodsname5)
    TextView goodsNameTv5;
    @BindView(R.id.pad_car_goodsname6)
    TextView goodsNameTv6;
    @BindView(R.id.pad_car_goodsname7)
    TextView goodsNameTv7;
    @BindView(R.id.pad_car_goodsname8)
    TextView goodsNameTv8;
    @BindView(R.id.pad_car_goodsname9)
    TextView goodsNameTv9;
    @BindView(R.id.pad_car_goodsname10)
    TextView goodsNameTv10;

    @BindView(R.id.pad_car_goodsprice1)
    TextView goodsPriceTv1;
    @BindView(R.id.pad_car_goodsprice2)
    TextView goodsPriceTv2;
    @BindView(R.id.pad_car_goodsprice3)
    TextView goodsPriceTv3;
    @BindView(R.id.pad_car_goodsprice4)
    TextView goodsPriceTv4;
    @BindView(R.id.pad_car_goodsprice5)
    TextView goodsPriceTv5;
    @BindView(R.id.pad_car_goodsprice6)
    TextView goodsPriceTv6;
    @BindView(R.id.pad_car_goodsprice7)
    TextView goodsPriceTv7;
    @BindView(R.id.pad_car_goodsprice8)
    TextView goodsPriceTv8;
    @BindView(R.id.pad_car_goodsprice9)
    TextView goodsPriceTv9;
    @BindView(R.id.pad_car_goodsprice10)
    TextView goodsPriceTv10;
    @BindView(R.id.pad_car_clear_0)
    Button clear0;//清除为0
    @BindView(R.id.pad_car_qp_btn)
    Button qpBtn;//去皮
    @BindView(R.id.pad_car_pz_tag)
    TextView pzTagTv;//显示皮重标识
    private Intent intentService;//双屏幕显示
    @BindView(R.id.pad_car_weight_tv)
    TextView weightTv;//电子秤称重重量
    //    @BindView(R.id.pad_car_bd_btn)
    TextView bd;//标定核准打印机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pad_car);
        activity = this;
//        weightTv = (TextView) findViewById(R.id.pad_car_weight_tv);
        intentService = new Intent(activity, XHShowService.class);
        startService(intentService);
        ButterKnife.bind(activity);
        EventBus.getDefault().register(this);
        initView();
        getData(typeIndexPage);
        new TimeThread().start();
//        new UartReadThread().start();
//        UartControlNative.initUartNative("/dev/ttyS4",9600);
        bd = (TextView) findViewById(R.id.pad_car_bd_btn);
        clear0.setOnClickListener(this);
        qpBtn.setOnClickListener(this);
        bd.setOnClickListener(this);
        initUart();
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        RequestReqMsgData.UpdateVersion(activity, null);
//        return view;
    }

    private void getData(int indexpage) {
        GetGoodsType(indexpage);
    }

    private void initView() {
        goodsBtn1.setOnClickListener(this);
        goodsBtn2.setOnClickListener(this);
        goodsBtn3.setOnClickListener(this);
        goodsBtn4.setOnClickListener(this);
        goodsBtn5.setOnClickListener(this);
        goodsBtn6.setOnClickListener(this);
        goodsBtn7.setOnClickListener(this);
        goodsBtn8.setOnClickListener(this);
        goodsBtn9.setOnClickListener(this);
        goodsBtn10.setOnClickListener(this);
        nowTimeTv.setText(SXUtils.getInstance(activity).GetNowDateTime() + "");
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.pad_car_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setEnabled(false);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
//                    indexPage = 0;
//                    initData();
                } else {
//                    indexPage ++;
//                    initData();
//                    HttpLiveSp(indexPage);
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.pad_car_goodslist_recyclv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initHandler();
        simpAdapter = new PadCarGoodsListRecyclerViewAdapter(activity, AppClient.padCarGoodsList);
        recyclerView.setAdapter(simpAdapter);
        goodsNumberTv.setText(AppClient.padCarGoodsList.size() + "件");
        totalPriceTv.setText(simpAdapter.getPadCarTotalMoney() + "元");
//        steData();
    }

    //    /**
//     * 测试模拟测试数据
//     */
//    private void steData(){
//        GoodsInfoEntity  shoppingCartLinesEntity;
//        for (int i=0;i<10;i++){
//            shoppingCartLinesEntity = new GoodsInfoEntity();
//            shoppingCartLinesEntity.setGoodsName("我是商品名称");
//            shoppingCartLinesEntity.setSkuPrice(i+"."+i);
//            shoppingCartLinesEntity.setQuantity("1");
//            shoppingCartLinesEntity.setGoodsModel("公斤");
//            AppClient.padCarGoodsList.add(shoppingCartLinesEntity);
//        }
////        hand.sendEmptyMessage(1009);
//    }
    private void initHandler() {
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        Typelist = (List<PadGoodsTypeGoodsEntity>) msg.obj;
                        if (Typelist.size() > 0) {
                            initTypeData(Typelist);
                        } else {
                            SXUtils.getInstance(activity).ToastCenter("未查询到数据");
                        }
                        break;
                    case 1009:
                        simpAdapter = new PadCarGoodsListRecyclerViewAdapter(activity, AppClient.padCarGoodsList);
                        recyclerView.setAdapter(simpAdapter);
                        goodsNumberTv.setText(AppClient.padCarGoodsList.size() + "件");
                        totalPriceTv.setText(simpAdapter.getPadCarTotalMoney() + "元");
                        break;
                    case AppClient.HANDLERLOCK:
                        if (LockDialog != null)
                            LockDialog.dismiss();
                        break;
                    case 10002:
                        simpAdapter.clearCar();
                        SXUtils.getInstance(activity).ToastCenter("订单结算成功，请扫码支付");
                        break;
                    case 000022:
                        String strweight = (String) msg.obj;
                        weightTv.setText(strweight);
                        break;
                    case 000024:
                        String tag = (String) msg.obj;
                        if (tag.equals("1")) {
                            pzTagTv.setText("有皮重");
                        } else {
                            pzTagTv.setText("无皮重");
                        }
                        break;
                    case AppClient.ERRORCODE:
                        String str = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(str + "");
                        break;
                }
                if (mSwipyRefreshLayout != null) {
                    mSwipyRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }

    private void initTypeData(List<PadGoodsTypeGoodsEntity> Typelist) {
        for (int i = 0; i < Typelist.size(); i++) {
            switch (i) {
                case 0:
                    typeBtn1.setText(Typelist.get(i).getCategoryName() + "");
                    break;
                case 1:
                    typeBtn2.setText(Typelist.get(i).getCategoryName());
                    break;
                case 2:
                    typeBtn3.setText(Typelist.get(i).getCategoryName());
                    break;
                case 3:
                    typeBtn4.setText(Typelist.get(i).getCategoryName());
                    break;
                case 4:
                    typeBtn5.setText(Typelist.get(i).getCategoryName());
                    break;
                case 5:
                    typeBtn6.setText(Typelist.get(i).getCategoryName());
                    break;
                case 6:
                    typeBtn7.setText(Typelist.get(i).getCategoryName());
                    break;
                case 7:
                    typeBtn8.setText(Typelist.get(i).getCategoryName());
                    break;
                case 8:
                    typeBtn9.setText(Typelist.get(i).getCategoryName());
                    break;
                case 9:
                    typeBtn10.setText(Typelist.get(i).getCategoryName());
                    break;
            }
        }
        initGoodsInfo(Typelist.get(0).getCategoryList());
    }

    private void initGoodsInfo(List<GoodsInfoEntity> categoryList) {
        clearGoodsBtn();
        for (int i = 0; i < categoryList.size(); i++) {
            switch (i) {
                case 0:
                    goodsNameTv1.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv1.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 1:
                    goodsNameTv2.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv2.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 2:
                    goodsNameTv3.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv3.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 3:
                    goodsNameTv4.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv4.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 4:
                    goodsNameTv5.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv5.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 5:
                    goodsNameTv6.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv6.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 6:
                    goodsNameTv7.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv7.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 7:
                    goodsNameTv8.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv8.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 8:
                    goodsNameTv9.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv9.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
                case 9:
                    goodsNameTv10.setText(categoryList.get(i).getGoodsName());
                    goodsPriceTv10.setText("￥" + categoryList.get(i).getChirdren().get(0).getShopPrice() + "/" + categoryList.get(i).getGoodsUnit());
                    break;
            }
        }
    }

    private void clearTypeBtn() {
        typeBtn1.setText("");
        typeBtn2.setText("");
        typeBtn3.setText("");
        typeBtn4.setText("");
        typeBtn5.setText("");
        typeBtn6.setText("");
        typeBtn7.setText("");
        typeBtn8.setText("");
        typeBtn9.setText("");
        typeBtn10.setText("");
    }

    private void clearGoodsBtn() {
        goodsPriceTv1.setText("");
        goodsNameTv1.setText("");
        goodsPriceTv2.setText("");
        goodsNameTv2.setText("");
        goodsPriceTv3.setText("");
        goodsNameTv3.setText("");
        goodsPriceTv4.setText("");
        goodsNameTv4.setText("");
        goodsPriceTv5.setText("");
        goodsNameTv5.setText("");
        goodsPriceTv6.setText("");
        goodsNameTv6.setText("");
        goodsPriceTv7.setText("");
        goodsNameTv7.setText("");
        goodsPriceTv8.setText("");
        goodsNameTv8.setText("");
        goodsPriceTv9.setText("");
        goodsNameTv9.setText("");
        goodsPriceTv10.setText("");
        goodsNameTv10.setText("");

    }

    private void setTextColor() {
        switch (typeBtnPosion) {
            case 0:
                typeBtn1.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 1:
                typeBtn2.setTextColor(Color.WHITE);
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 2:
                typeBtn3.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 3:
                typeBtn4.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 4:
                typeBtn5.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 5:
                typeBtn6.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 6:
                typeBtn7.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 7:
                typeBtn8.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 8:
                typeBtn9.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn10.setTextColor(getResources().getColor(R.color.col_333));
                break;
            case 9:
                typeBtn10.setTextColor(Color.WHITE);
                typeBtn2.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn3.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn4.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn5.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn6.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn7.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn8.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn9.setTextColor(getResources().getColor(R.color.col_333));
                typeBtn1.setTextColor(getResources().getColor(R.color.col_333));
                break;


        }
    }

    @OnClick({R.id.pad_car_update_number_btn, R.id.pad_car_update_price_btn, R.id.pad_car_update_del_btn, R.id.pad_car_update_clear_btn,
            R.id.pad_car_update_lock_btn, R.id.pad_car_topay_btn, R.id.pad_car_goods_manage_btn,
            R.id.pad_car_type1, R.id.pad_car_type2, R.id.pad_car_type3, R.id.pad_car_type4, R.id.pad_car_type5,
            R.id.pad_car_type6, R.id.pad_car_type7, R.id.pad_car_type8, R.id.pad_car_type9, R.id.pad_car_type10,

            R.id.pad_car_type_last_setp, R.id.pad_car_type_next,
//

    })
    public void OnclickBtn(Button button) {
        switch (button.getId()) {
            case R.id.pad_car_update_number_btn:
                InputDailog("0", "请输入修改数量");
                break;
            case R.id.pad_car_update_price_btn:
                InputDailog("1", "请输入修改价格");
                break;
            case R.id.pad_car_update_del_btn:
                simpAdapter.removeData();
                break;
            case R.id.pad_car_update_clear_btn:
//                send("123123213123123131321\ndsfssdfdsfdsf\n\r");
                simpAdapter.clearCar();
                break;
            case R.id.pad_car_update_lock_btn:
                LockDailog();
                break;
            case R.id.pad_car_goods_manage_btn:
                Intent intent = new Intent(activity, GoodsListFragment.class);
                startActivity(intent);
                break;
            case R.id.pad_car_topay_btn:
                if (AppClient.padCarGoodsList.size() > 0) {
                    testPrint(AppClient.padCarGoodsList);
                }
//                if(AppClient.padCarGoodsList.size()>0){
//                    SXUtils.getInstance(activity).GoPay(hand,
//                            AppClient.padCarGoodsList.size()+"",
//                            simpAdapter.getPadCarTotalMoney(),
//                            simpAdapter.getPadCarTotalWeight(),
//                            "18682136973",//电子秤重量 型号
//                            simpAdapter.getSkuList());
//                }else{
//                    SXUtils.getInstance(activity).ToastCenter("请添加商品进行结账");
//                }
                break;
            case R.id.pad_car_type1:
                typeBtnPosion = 0;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {
                }

                break;
            case R.id.pad_car_type2:
                typeBtnPosion = 1;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type3:
                typeBtnPosion = 2;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type4:
                typeBtnPosion = 3;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type5:
                typeBtnPosion = 4;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type6:
                typeBtnPosion = 5;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type7:
                typeBtnPosion = 6;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type8:
                typeBtnPosion = 7;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type9:
                typeBtnPosion = 8;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type10:
                typeBtnPosion = 9;
                try {
                    initGoodsInfo(Typelist.get(typeBtnPosion).getCategoryList());
                    setTextColor();
                } catch (Exception e) {

                }
                break;
            case R.id.pad_car_type_last_setp:
                break;
            case R.id.pad_car_type_next:
                break;

            case R.id.pad_car_goods_last_step:

                break;
            case R.id.pad_car_goods_next:
                break;
            default:
                break;
        }
    }

    private static OutputStream outputStream = null;
//    public void send(String sendData) {
//        System.out.println("开始打印！！");
//        try {
//            byte[] data = sendData.getBytes("UTF-8");
//            UartControlNative.uartSendMessageNative(data);
////                outputStream.write(data, 0, data.length);
////                outputStream.flush();
//        } catch (IOException e) {
//        }
//    }


    private void addCarGoods() {
        try {
            GoodsInfoEntity goodsinfo = Typelist.get(typeBtnPosion).getCategoryList().get(goodsBtnPosion);
            ShoppingCartLinesEntity shoppingCartLinesEntity = new ShoppingCartLinesEntity();
            shoppingCartLinesEntity.setQuantity("");
            shoppingCartLinesEntity.setGoodsWeight(goodsinfo.getChirdren().get(0).getGoodsWeight() + "");
            shoppingCartLinesEntity.setGoodsCode(goodsinfo.getGoodsCode() + "");
            shoppingCartLinesEntity.setGoodsName(goodsinfo.getGoodsName() + "");
            shoppingCartLinesEntity.setGoodsModel(goodsinfo.getChirdren().get(0).getGoodsModel() + "");
            shoppingCartLinesEntity.setGoodsUnit(goodsinfo.getGoodsUnit() + "");
            shoppingCartLinesEntity.setSkuBarcode(goodsinfo.getChirdren().get(0).getSkuBarcode() + "");
            shoppingCartLinesEntity.setSkuPrice(goodsinfo.getChirdren().get(0).getShopPrice() + "");
            int posion = isExistGoods(goodsinfo.getGoodsCode());
            if (posion > -1) {
                ShoppingCartLinesEntity shop = AppClient.padCarGoodsList.get(posion);
//                if(!TextUtils.isEmpty(shop.getGoodsWeight())){
//                    shop.setGoodsWeight(SXUtils.getInstance(activity).priceTwoNum(goodsinfo.getChirdren().get(0).getGoodsWeight()));
//                }else{
//                    float  strWeight = Float.parseFloat(shop.getGoodsWeight());
//                    float  addprice = Float.parseFloat(goodsinfo.getChirdren().get(0).getGoodsWeight());
//                }
                float exist = Float.parseFloat(shop.getGoodsWeight());
                float newStr = Float.parseFloat(goodsinfo.getChirdren().get(0).getGoodsWeight());
                shop.setGoodsWeight(exist + newStr + "");
            } else {
                AppClient.padCarGoodsList.add(shoppingCartLinesEntity);
            }
        } catch (Exception e) {
            return;
        }
        simpAdapter.notifyDataSetChangedSetCarTotalPrice();
    }

    /**
     * @param goodsCode
     * @return true  存在
     */
    private int isExistGoods(String goodsCode) {
        int posiont = -1;
        for (int i = 0; i < AppClient.padCarGoodsList.size(); i++) {
            if (AppClient.padCarGoodsList.get(i).getGoodsCode().equals(goodsCode)) {
                posiont = i;
            }

        }
        return posiont;
    }

    /**
     * 输入框弹窗
     *
     * @param isprice 0 修改数量  1 修改价格
     * @param title
     */
    public void InputDailog(final String isprice, final String title) {
        if (simpAdapter.mSelect < 0) {
            SXUtils.getInstance(activity).ToastCenter("请选择商品进行修改");
            return;
        }
        View convertView = LayoutInflater.from(activity).inflate(R.layout.pad_car_update_value_dialog, null);
        TextView titleTV = (TextView) convertView.findViewById(R.id.pad_car_update_dialog_title);
        titleTV.setText(title);
        final EditText et = (EditText) convertView.findViewById(R.id.pad_car_update_dialog_edt);
        new AlertDialog.Builder(activity)
//                .setTitle(title+"")
//                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(convertView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(activity, title + "" + input, Toast.LENGTH_LONG).show();
                        } else {
                            simpAdapter.updateData(isprice, input);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    Dialog LockDialog;

    /**
     * 用户锁屏幕操作
     */
    public void LockDailog() {
        LockDialog = new AlertDialog.Builder(activity).create();
        LockDialog.show();
        LockDialog.setCancelable(false);
        LockDialog.setCanceledOnTouchOutside(false);
        Window window = LockDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        window.setContentView(R.layout.pad_car_lock_dialog);
        final EditText inputedt = (EditText) window.findViewById(R.id.pad_car_lock_input_psd_tv);
        TextView cancelTv = (TextView) window.findViewById(R.id.pad_car_lock_cancel_tv);
        TextView unlockTv = (TextView) window.findViewById(R.id.pad_car_unlock_cancel_tv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LoginNameActivity.class);
                startActivity(intent);
                activity.finish();
//                System.exit(0);

            }
        });
        unlockTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd = inputedt.getText().toString();
                if (TextUtils.isEmpty(psd)) {
                    SXUtils.getInstance(activity).ToastCenter("请输入解锁密码");
                    return;
                }
                SXUtils.showMyProgressDialog(activity, false);
                SXUtils.getInstance(activity).Unlock(hand, psd);
            }
        });
    }

    /**
     * 查询销售目录下所有二级分类及下面的商品
     */
    public void GetGoodsType(int pageIndex) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("pageSize", "12");
        httpParams.put("pageIndex", pageIndex + "");
        HttpUtils.getInstance(activity).requestPost(false, AppClient.SELECTSALE, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("平板商品分类发送成功返回参数=======", jsonObject.toString());
                List<PadGoodsTypeGoodsEntity> goodsTypeList = null;
                try {
                    goodsTypeList = ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(), PadGoodsTypeGoodsEntity.class);
                } catch (Exception e) {
                    Logs.i(e.toString());
                }
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = goodsTypeList;
                hand.sendMessage(msg);
            }

            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pad_car_goods1:
                goodsBtnPosion = 0;
                addCarGoods();
                break;
            case R.id.pad_car_goods2:
                goodsBtnPosion = 1;
                addCarGoods();
                break;
            case R.id.pad_car_goods3:
                goodsBtnPosion = 2;
                addCarGoods();
                break;
            case R.id.pad_car_goods4:
                goodsBtnPosion = 3;
                addCarGoods();
                break;
            case R.id.pad_car_goods5:
                goodsBtnPosion = 4;
                addCarGoods();
                break;
            case R.id.pad_car_goods6:
                goodsBtnPosion = 5;
                addCarGoods();
                break;
            case R.id.pad_car_goods7:
                goodsBtnPosion = 6;
                addCarGoods();
                break;
            case R.id.pad_car_goods8:
                goodsBtnPosion = 7;
                addCarGoods();
                break;
            case R.id.pad_car_goods9:
                goodsBtnPosion = 8;
                addCarGoods();
                break;
            case R.id.pad_car_goods10:
                goodsBtnPosion = 9;
                addCarGoods();
                break;
            case R.id.pad_car_clear_0:
                UartOptNative.uartSendMessageNative(scale_fb, ZhilingTest.ZL());
                //置零按钮
//                testPrint();
                break;
            case R.id.pad_car_qp_btn:
                UartOptNative.uartSendMessageNative(scale_fb, ZhilingTest.QP());
                //去皮
                break;
            case R.id.pad_car_bd_btn:
                byte[] senddata = ZhilingTest.PricbD();
//                byte[] senddata = Utils.hexStringToBytes(SendString);
////                串口发送十六进制，
                //标定
                UartOptNative.uartSendMessageNative(scale_fb, senddata);
                break;
        }
    }

    /**
     * 更新当前时间
     */
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    EventBus.getDefault().post(new MessageEvent(AppClient.PADEVENT00002, ""));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private IReadDataListener listener;
    private UartReadThread mUartReadThread;
    private int scale_fb;
    private int print_fb;
    private Button button;
    private PrintUtils print;

    /**
     * 初始化打印机和电子秤串口
     */
    private void initUart() {
        scale_fb = UartOptNative.openUartNative("/dev/ttyS1", 9600);
        Logs.i("+++++++++++", "fb = " + scale_fb);
        print_fb = UartOptNative.openUartNative("/dev/ttyS4", 9600);
        Logs.i("+++++++++++", "fb = " + print_fb);

        if (scale_fb > 0) {
            startUartReadThread();
        }
//        testPrint();
    }

    private void startUartReadThread() {
        if (mUartReadThread == null) {
            mUartReadThread = new UartReadThread(scale_fb);
            mUartReadThread.setListener(this);
            mUartReadThread.start();
        }
    }

    private void stopUartReadThread() {
        if (mUartReadThread != null) {
            mUartReadThread.stopThread();
            mUartReadThread = null;
        }
    }

    /**
     * 打印机执行打印
     *
     * @param shopcarList
     */
    private void testPrint(List<ShoppingCartLinesEntity> shopcarList) {
        if (print_fb > 0) {
            if (print == null) {
                print = new PrintUtils(print_fb, "1");
            }
            try {
                print.standardPrinterLine(SXUtils.getInstance(activity).GetNowDateTime() + "", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardPrinterLine("收款：" + "" + simpAdapter.getPadCarTotalMoney() + "       " + "优惠：" + "0.00", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardPrinterLine("总计：" + "" + simpAdapter.getPadCarTotalMoney() + "       " + "应收：" + "0.00", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardPrinterLine("件数：" + "" + AppClient.padCarGoodsList.size() + "        " + "找回：" + "0.00", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardPrinterLine("-----------------------------", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                for (int i = 0; i < shopcarList.size(); i++) {
                    print.standardPrinterLine(shopcarList.get(i).getGoodsName() + "   " + shopcarList.get(i).getGoodsWeight() + "   " + shopcarList.get(i).getSkuPrice() + "    " + simpAdapter.initdoublw(Float.parseFloat(shopcarList.get(i).getGoodsWeight()) * Float.parseFloat(shopcarList.get(i).getSkuPrice()) + "") + "", PrintUtils.CENTER);
                    print.standardBoldPrinterLine("", PrintUtils.CENTER);
                }
//                print.standardPrinterLine("青菜  2/公斤  12.0元   65.0元", PrintUtils.CENTER);
//                print.standardBoldPrinterLine("",PrintUtils.CENTER);
//                print.standardPrinterLine("青菜  7/公斤  12.0元   34.0元", PrintUtils.CENTER);
//                print.standardBoldPrinterLine("",PrintUtils.CENTER);
//                print.standardPrinterLine("青菜  9/公斤  12.0元    4.0元", PrintUtils.CENTER);
//                print.standardBoldPrinterLine("",PrintUtils.CENTER);
//                print.standardPrinterLine("青菜  3/公斤  12.0元   12.0元", PrintUtils.CENTER);
//                print.standardBoldPrinterLine("",PrintUtils.CENTER);
//                print.standardPrinterLine("青菜  2/公斤  12.0元   12.0元", PrintUtils.CENTER);
//                print.standardBoldPrinterLine("",PrintUtils.CENTER);
                print.standardPrinterLine("-----------------------------", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardBoldPrinterLine("名称   重量    单价     总价", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);

                print.billHeaderPrinter("皕鲜自营店");
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
                print.standardBoldPrinterLine("", PrintUtils.CENTER);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReadData(String data) {
        String strTag = "0";
        // 处理数据
        if (data.indexOf("4d3e505441") >= 0) {
            Message msg = new Message();
            msg.what = 000023;
            msg.obj = "去皮指令发送成功";
            hand.sendMessage(msg);
        } else if (data.indexOf("4d3e505a4f") >= 0) {
            Message msg = new Message();
            msg.what = 000023;
            msg.obj = "置零指令发送成功";
            hand.sendMessage(msg);
        } else if (data.indexOf("4d3e504244") >= 0) {
            Message msg = new Message();
            msg.what = 000023;
            msg.obj = "定标指令发送成功";
            hand.sendMessage(msg);
        } else if (data.length() == 20) {
            byte[] weitht = Utils.hexStringToBytes(data);
            if ((weitht[1] & 0x10) == 0x10) {
                // 超重
            }
            if ((weitht[1] & 0x04) == 0x04) {
                Logs.i("======稳定+000000");
                // 稳定
            }
            if ((weitht[1] & 0x08) == 0x08) {
                // 重量数据为正数
            }
            if ((weitht[1] & 0x08) == 0x00) {
                strTag = "1";
                // 重量数据 单位克
//                float v = (float) (Utils.bytesToInt2(weitht, 2)/1000.0);
//                // 重量数据为负数
//                Message msg = new Message();
//                msg.what = 000022;
//                msg.obj = "-"+v+"";
//                hand.sendMessage(msg);
//                return ;
            }
            if ((weitht[1] & 0x02) == 0x02) {
                // 去皮
                Logs.i("=========去皮11111111111");
            }
            if ((weitht[1] & 0x01) == 0x01) {
                // 置零
                Logs.i("=========+置零222222222");
            }
            if ((weitht[1] & 0x01) == 0x01) {
                //标定成功
                Logs.i("=========+置零33333");
            }
            // 重量数据 单位克
            float v = (float) (Utils.bytesToInt2(weitht, 2) / 1000.0);
            // 皮重 单位克
            float p = (float) (Utils.bytesToInt2(weitht, 6) / 1000.0);
            if (p > 0) {
                //有皮重显示 皮重表示
                Message msg = new Message();
                msg.what = 000024;
                msg.obj = "1";
                hand.sendMessage(msg);
            } else {
                //有皮重显示 皮重表示
                Message msg = new Message();
                msg.what = 000024;
                msg.obj = "2";
                hand.sendMessage(msg);
            }
            Message msg = new Message();
            msg.what = 000022;
            if (strTag.equals("1")) {
                msg.obj = "-" + v + "";
            } else {
                msg.obj = v + "";
            }
            hand.sendMessage(msg);

//            Logs.i("======"+"重量 = " + v / 1000.0);
            Logs.i("重量 = " + v + " 皮重 = " + p);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if (messageEvent.getTag() == AppClient.PADEVENT00001) {
            goodsNumberTv.setText(AppClient.padCarGoodsList.size() + "件");
            totalPriceTv.setText(simpAdapter.getPadCarTotalMoney() + "元");
        } else if (messageEvent.getTag() == AppClient.PADEVENT00002) {
            nowTimeTv.setText(SXUtils.getInstance(activity).GetNowDateTime() + "");
        }
        if (messageEvent.getTag() == 6666) {
            progressBar.setProgress(Integer.parseInt(messageEvent.getMessage()));
            progressnumTv.setText(messageEvent.getMessage() + "%");
            if (Integer.parseInt(messageEvent.getMessage()) == 100) {
                if (!mustUpdate.equals("1")) {
                    if (proDialog != null) {
                        proDialog.dismiss();
                    }
                }
            }

        } else if (messageEvent.getTag() == AppClient.UPDATEVER) {
            String message = messageEvent.getMessage();
//            "responseData":{"forceUpdate":"true","upgradeDesc":"xxx","upgradeUrl":"http://spa.xianhao365.com/..."}
            String content = "";
            String updateUrl = null;
            String forceUpdate = null;
            String version = null;
            try {
                JSONObject jsonObject = new JSONObject(message);
                content = jsonObject.getString("upgradeDesc");
                updateUrl = jsonObject.getString("upgradeUrl");
                forceUpdate = jsonObject.getString("forceUpgrade");
                version = jsonObject.getString("softwareVersion");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String updateStr = updateUrl;
            mustUpdate = forceUpdate;
            final boolean isMustUpdate;
            if (!mustUpdate.equals("1"))  //1 强制更新  0 不强制更新
            {
                isMustUpdate = true;
            } else {
                isMustUpdate = false;
            }
            SXUtils.getInstance(activity).UpdateConfrimDialogView(activity, isMustUpdate, "  版本更新\n新版本:" + version + "", content + "", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mustUpdate.equals("1")) {
                        SXUtils.getInstance(activity).tipDialog.dismiss();
                    }
                    UpdateProgressDialog(activity, isMustUpdate, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!mustUpdate.equals("1")) {
                                proDialog.dismiss();
                            } else {
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
                    DownloadOkHttpUtils.DownFile(activity, updateStr, progressBar, progressnumTv);
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
        stopUartReadThread();
    }

    String mustUpdate;
    AlertDialog proDialog;
    ProgressBar progressBar;
    TextView progressnumTv;
    TextView cancleTv;

    /**
     * 下载apk更新进度条显示框
     *
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
        if (!cancelable) {
            cancleTv.setText("退出皕鲜");
        } else {
            cancleTv.setText("取消下载");
        }
        cancleTv.setOnClickListener(onClickListener);

    }
}