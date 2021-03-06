package com.wyzk.lottery.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wyzk.lottery.R;
import com.wyzk.lottery.model.ResultReturn;
import com.wyzk.lottery.model.RoomModel;
import com.wyzk.lottery.network.Network;
import com.wyzk.lottery.utils.BuildManager;
import com.wyzk.lottery.view.MyRadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 设置抽成比例 0-10%
 */
public class SetRateActivity extends LotteryBaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title)
    View title;
    @Bind(R.id.mygroup)
    MyRadioGroup myGroup;
    @Bind(R.id.ling)
    RadioButton ling;
    @Bind(R.id.yi)
    RadioButton yi;
    @Bind(R.id.er)
    RadioButton er;
    @Bind(R.id.san)
    RadioButton san;
    @Bind(R.id.si)
    RadioButton si;
    @Bind(R.id.wu)
    RadioButton wu;
    @Bind(R.id.liu)
    RadioButton liu;
    @Bind(R.id.qi)
    RadioButton qi;
    @Bind(R.id.ba)
    RadioButton ba;
    @Bind(R.id.jiu)
    RadioButton jiu;
    @Bind(R.id.shi)
    RadioButton shi;

    private int percentage = 0;
    private int roomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rate);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.arrow_back);
        setSupportActionBar(toolbar);
        BuildManager.setStatusTrans(this, 1, title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.ling:
                        percentage = 0;
                        break;
                    case R.id.yi:
                        percentage = 1;
                        break;
                    case R.id.er:
                        percentage = 2;
                        break;
                    case R.id.san:
                        percentage = 3;
                        break;
                    case R.id.si:
                        percentage = 4;
                        break;
                    case R.id.wu:
                        percentage = 5;
                        break;
                    case R.id.liu:
                        percentage = 6;
                        break;
                    case R.id.qi:
                        percentage = 7;
                        break;
                    case R.id.ba:
                        percentage = 8;
                        break;
                    case R.id.jiu:
                        percentage = 9;
                        break;
                    case R.id.shi:
                        percentage = 10;
                        break;
                }
            }
        });
        roomId = (Integer) extraBean.getData();

        getRoom();
    }

    private void getRoom() {
        subscription = Network.getNetworkInstance().getLiveApi()
                .myRoom(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultReturn<RoomModel.RowModel>>() {
                    @Override
                    public void accept(ResultReturn<RoomModel.RowModel> result) throws Exception {
                        if (result.getCode() == ResultReturn.ResultCode.RESULT_OK.getValue()) {
                            setChecked(result.getData().getPercentage());
                        }
                        dismissLoadingView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoadingView();
                    }
                });
    }


    private void setChecked(int percentage) {
        myGroup.clearCheck();
        switch (percentage) {
            case 0:
                myGroup.check(R.id.ling);
                break;
            case 1:
                myGroup.check(R.id.yi);
                break;
            case 2:
                myGroup.check(R.id.er);
                break;
            case 3:
                myGroup.check(R.id.san);
                break;
            case 4:
                myGroup.check(R.id.si);
                break;
            case 5:
                myGroup.check(R.id.wu);
                break;
            case 6:
                myGroup.check(R.id.liu);
                break;
            case 7:
                myGroup.check(R.id.qi);
                break;
            case 8:
                myGroup.check(R.id.ba);
                break;
            case 9:
                myGroup.check(R.id.jiu);
                break;
            case 10:
                myGroup.check(R.id.shi);
                break;
        }
    }

    public void submit(View view) {
        set(percentage);
    }

    private void set(final int percentage) {
        subscription = Network.getNetworkInstance().getLiveApi()
                .updateRoomPercentage(token, roomId + "", percentage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultReturn<String>>() {
                    @Override
                    public void accept(ResultReturn<String> stringResultReturn) throws Exception {
                        showToast(getString(R.string.set_succ));
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoadingView();
                        showToast(getString(R.string.set_fail));
                    }
                });
    }
}
