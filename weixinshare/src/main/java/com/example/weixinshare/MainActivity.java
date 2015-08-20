package com.example.weixinshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class MainActivity extends ActionBarActivity {

    /**
     * App唯一标识
     * 在微信开放平台上申请账号，创建移动app审核通过后，可以获得app_id
     * wx2fb682b1dce9d873用的是其他人的，进行测试
     */
    public static final String APP_ID = "wx2fb682b1dce9d873";

    /**
     * 将微信集成为一个具体的对象,实现微信功能的调用
     */
    private IWXAPI api;

    private CheckBox mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        mShared = (CheckBox) this.findViewById(R.id.checkBox_share);

        //将微信整个看做是一个对象，api在这就表示微信的对应
        api = WXAPIFactory.createWXAPI(this, APP_ID);
    }

    /**
     * 启动微信的点击事件
     *
     * @param view
     */
    public void Button_Laucher_Weixin(View view) {
        //使用微信实例化对象，启动微信；
        Toast.makeText(this, String.valueOf(api.openWXApp()), Toast.LENGTH_LONG).show();
    }

    /**
     * 向朋友或者朋友圈，发送文本
     *
     * @param view
     */
    public void Buuton_Send_Text(View view) {
        final EditText editText = new EditText(this);
        editText.setText("请输入发送文本");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("分享文本");
        builder.setView(editText);
        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //分享文本信息

                String text = editText.getText().toString();

                //第一步：创建WXTextObject，用于存储文本信息
                WXTextObject textObject = new WXTextObject();
                textObject.text = text;

                //第二步：创建WXMediaMessage 用于发送消息的封装
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObject;
                msg.description = text;

                //第三步：创建发送请求SendMessageToWX.Req，用于给微信客户端发送消息
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                //封装发送消息
                req.message = msg;
                //设置发送唯一表示
                req.transaction = buildTransaction("文本");
                //设置发送场景
                req.scene = mShared.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

                //第四步：向微信客户端发送信息
                api.registerApp(APP_ID);//注册发送的唯一标识
                api.sendReq(req);//发送请求

            }
        });
        builder.setNegativeButton("取消", null);

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * 创建唯一标识
     * @param type
     * @return
     */
    private String buildTransaction(final String type){
        return type==null?String.valueOf(System.currentTimeMillis()):type+String.valueOf(System.currentTimeMillis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
