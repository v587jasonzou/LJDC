package com.ljdc.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.ljdc.R;
import com.ljdc.app.Config;
import com.ljdc.utils.Act;

public class PersonInfoActivity extends Activity implements View.OnClickListener {

    private  SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initView();

    }

    private void initView() {
        findViewById(R.id.logout).setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                // 设置数据库为未初始化状态，再次登录需要重新初始化
                sp = getSharedPreferences(Config.SP_LOGIN_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(Config.SP_IS_LOGIN,false);
                editor.putBoolean(Config.SP_IS_DATABASE_INITED,false);
                editor.commit();
                Act.toAct(this,LoginActivity.class);
                this.finish();
                break;
        }
    }
}
