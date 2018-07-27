package com.wyzk.lottery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.fsix.mqtt.MQ;
import com.fsix.mqtt.bean.MQBean;
import com.fsix.mqtt.bean.MqttConnBean;
import com.fsix.mqtt.observer.EventManager;
import com.fsix.mqtt.observer.INotify;
import com.fsix.mqtt.util.ATil;
import com.wyzk.lottery.R;
import com.wyzk.lottery.constant.IConst;
import com.wyzk.lottery.event.NetworkEvent;
import com.wyzk.lottery.model.ExtraBean;
import com.wyzk.lottery.utils.ACache;
import com.wyzk.lottery.utils.AppTools;
import com.wyzk.lottery.utils.Base64;
import com.wyzk.lottery.utils.SharePreferencesUtil;
import com.wyzk.lottery.view.LoadingView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Subscription;

public abstract class LotteryBaseActivity extends AppCompatActivity implements INotify<MQBean> {
    public final static String VALUE_KEY = "ExtraBean";
    protected Subscription subscription;
    protected Subscription subscription2;
    protected ExtraBean extraBean;
    protected String token;
    private static MQ mq;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventManager.getInstance().registerObserver(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            extraBean = (ExtraBean) bundle.getSerializable(VALUE_KEY);
        }

        token = ACache.get(this).getAsString(IConst.TOKEN);

        EventBus.getDefault().register(this);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkEvent networkEvent) {
        if (networkEvent.getAvailable()) {
            //网络可用
            networkAvailable();
        } else {
            //网络不可用
            Toast.makeText(this, "网络不稳定，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

    protected void networkAvailable() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
        EventManager.getInstance().unregisterObserver(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onNotify(MQBean eventData) {

    }

    protected void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if (subscription2 != null && !subscription2.isUnsubscribed()) {
            subscription2.unsubscribe();
        }
    }

    public void toActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void toActivityWithExtra(ExtraBean extraBean, Class<?> desActivity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VALUE_KEY, extraBean);
        AppTools.startForwardActivity(this, desActivity, bundle, false);
    }

    public void toCall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.call)));
        startActivity(intent);
    }

    /**
     * 显示一个Toast信息
     *
     * @param text
     */
    protected void showToast(final String text) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(LotteryBaseActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void tips(String msg) {
        showToast(msg);
    }

    protected View inflateSubView(int subId, int inflateId) {
        View noNetSubTree = findViewById(inflateId);
        if (noNetSubTree == null) {
            ViewStub viewStub = (ViewStub) findViewById(subId);
            noNetSubTree = viewStub.inflate();
        }
        noNetSubTree.setVisibility(View.VISIBLE);
        return noNetSubTree;
    }

    protected void showLoadingView() {
        View view = inflateSubView(R.id.activity_loading_stub,
                R.id.activity_loading_subTree);
        if (view != null) {
            LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
            if (loadingView != null) {
                loadingView.showLoading(true, R.string.loading_busy, 0);
            }
        }
    }

    protected void dismissLoadingView() {
        View view = findViewById(R.id.activity_loading_subTree);
        if (view != null) {
            LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
            if (loadingView != null) {
                loadingView.hidenLoading();
            }
        }
    }

    protected <T> T getSp(String key) {
        return Base64.strBase64Obj(SharePreferencesUtil.getString(this, key));
    }

    protected <T> void setSp(String key, T data) {
        SharePreferencesUtil.putString(this, key, Base64.objBase64Str(data));
    }

    /**
     * 退出登录
     */
    protected void logout() {
        ACache.get(this).clear();
        toActivity(LoginActivity.class);
        finish();
    }

    /**
     * 开启Mq服务
     */
    protected void startMqtt() {
        if (mq == null) {
            mq = new MQ();
            MqttConnBean mqttConnBean = new MqttConnBean();
            mqttConnBean.setBrokerUrl(IConst.BROKEURL);
            mqttConnBean.setClientId(ATil.getDeviceId(this));
            mqttConnBean.setUserName(IConst.USERNAME);
            mqttConnBean.setPassword(IConst.PASSWORD);
            mqttConnBean.setQos(IConst.QOS);
            mq.start(this, mqttConnBean, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    if (mq != null) {
                        mq.subscribe();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
        }
    }

    /**
     * 停止Mq服务
     */
    protected void stopMqtt() {
        if (mq != null) {
            mq.stop();
            mq = null;
        }
    }

    protected void subscribeMqTopic(String topic) {
        if (mq != null) {
            mq.subscribe(topic, IConst.QOS);
        }
    }
}