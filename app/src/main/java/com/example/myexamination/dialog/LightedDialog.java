package com.example.myexamination.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myexamination.R;

public class LightedDialog extends Dialog {
    Context mContext;
    private Button btnSure;
    private Button btnCancel;

    public LightedDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        initView();
    }

    //初始化
    public void initView() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.lighted_dialog, null);
        btnSure = view.findViewById(R.id.dialog_confirm_sure);
        btnCancel = view.findViewById(R.id.dialog_confirm_cancel);
        super.setContentView(view);
    }

    //确定键监听器
    public void setOnSureListener(View.OnClickListener listener) {
        btnSure.setOnClickListener(listener);
    }

    //取消键监听器
    public void setOnCancelListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }
}
