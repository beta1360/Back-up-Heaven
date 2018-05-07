package com.example.daekyo.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class LocalDialog extends Dialog{
    private ListView upper, lower;
    private Button enter, cancel;
    private int upper_num, lower_num;

    public LocalDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.local);

        upper = (ListView) findViewById(R.id.upper_local);
        lower = (ListView) findViewById(R.id.lower_local);

        enter = (Button) findViewById(R.id.local_enter);
        cancel = (Button) findViewById(R.id.local_cancel);

        enter.setOnClickListener(enterClick);
        cancel.setOnClickListener(cancelClick);
    }

    Button.OnClickListener enterClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    Button.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


}
