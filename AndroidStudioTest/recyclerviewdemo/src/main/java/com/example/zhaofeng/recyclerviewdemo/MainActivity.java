package com.example.zhaofeng.recyclerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<String> datas;

    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();

        initViews();

        simpleAdapter = new SimpleAdapter(datas,this);
        simpleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(MyHolderView view, int position) {
                Toast.makeText(getBaseContext(),"onItemClick",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(MyHolderView view, int position) {
                Toast.makeText(getBaseContext(),"onItemLongClick",Toast.LENGTH_LONG).show();
            }
        });

        //设置RecyclerView的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setAdapter(simpleAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置动画效果，新增或者删除
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的item分割线
        //使用公共资源https://github.com/gabrielemariotti/RecyclerViewItemAnimators
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
//        recyclerView.addItemDecoration(itemDecoration);
    }

    private void initDatas(){
        datas = new ArrayList<String>();
        for (int i = 'A'; i<'z';i++ ){
            datas.add(""+(char)i);
        }
    }
    private void initViews(){
        recyclerView = (RecyclerView) this.findViewById(R.id.id_recycler);
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

        switch (id){
            case R.id.action_add:
                simpleAdapter.addData(1);
                break;
            case R.id.action_delete:
                simpleAdapter.deleteData(1);
                break;
            case R.id.action_listview:
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                break;
            case R.id.action_gradview:
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                break;
            case R.id.action_hor_gradview:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.action_staggered:
                Intent intent = new Intent(this,StaggeredActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
