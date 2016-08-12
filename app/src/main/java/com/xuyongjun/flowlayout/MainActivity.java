package com.xuyongjun.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xuyongjun.flowlayout.view.FlowLayout;

public class MainActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;

    private String[] mValus = {
            "诛仙-今日新服","Prisma艺术相机","捕鱼达人千炮版",
            "悦动圈","芒果TV","幻城(正版授权)","美拍","去哪儿旅行"
            ,"网易云音乐","新浪微博","QQ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowLayout = (FlowLayout) findViewById(R.id.id_flowlayout);

        initData();
    }

    private void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this);

        for (int i = 0; i < mValus.length; i++) {
//            Button button = new Button(this);

//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);

//            button.setLayoutParams(lp);
//
//            button.setText(mValus[i]);
//
//            mFlowLayout.addView(button);

            TextView view = (TextView) mInflater.inflate(R.layout.item_flow, mFlowLayout, false);
            final String str = mValus[i];
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                }
            });
            view.setText(mValus[i]);
            mFlowLayout.addView(view);
        }
    }
}
