package com.example.zhaofeng.treeviewdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhaofeng.treeviewdemo.bean.FileBean;
import com.example.zhaofeng.treeviewdemo.tree.bean.Node;
import com.example.zhaofeng.treeviewdemo.tree.iterface.OnTreeClickListener;
import com.example.zhaofeng.treeviewdemo.tree.utils.SimpleTreeAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView listView ;
    private List<FileBean> mDatas;

    private static int newId = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) this.findViewById(R.id.id_listview);
        initData();
        Log.e("MainActivity", "mDatas.size() = " + mDatas.size());
        try {
            final SimpleTreeAdapter adapter = new SimpleTreeAdapter(this, listView, mDatas, 0);
            adapter.setOnTreeClickListener(new OnTreeClickListener() {
                @Override
                public void onTreeItemClick(Node node, int position) {
                    if (node.isLeaf()) {
                        Toast.makeText(MainActivity.this, "叶子节点", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onTreeItemLongClick(final Node node, int position) {
                    final EditText editText = new EditText(MainActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("增加子项")
                            .setView(editText)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!"".equals(editText.getText().toString())) {
                                        mDatas.add(new FileBean(newId++, node.getId(), editText.getText().toString()));
                                        adapter.refreshData(mDatas);
                                    } else {
                                        Toast.makeText(MainActivity.this, "当前项不能为空！！！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    builder.create().show();
                }
            });
            listView.setAdapter(adapter);
        }catch (Exception e){
            Log.e("MainActivity", "适配器配置失败！");
        }
    }

    private void initData() {
        mDatas = new ArrayList<FileBean>();
        FileBean bean0 = new FileBean(0,-1,"跟目录-0");
        mDatas.add(bean0);
        //
        FileBean bean0_0 = new FileBean(4,0,"子目录-0-0");
        mDatas.add(bean0_0);

        FileBean bean0_0_0 = new FileBean(6,4,"子目录-0-0-0");
        mDatas.add(bean0_0_0);

        FileBean bean0_1 = new FileBean(5,0,"子目录-0-1");
        mDatas.add(bean0_1);

        FileBean bean0_1_0 = new FileBean(7,5,"子目录-0-1-0");
        mDatas.add(bean0_1_0);



        FileBean bean1 = new FileBean(1,-1,"跟目录-1");
        mDatas.add(bean1);
        FileBean bean2 = new FileBean(2,-1,"跟目录-2");
        mDatas.add(bean2);
        FileBean bean3 = new FileBean(3,-1,"跟目录-3");
        mDatas.add(bean3);
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
