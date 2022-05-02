package com.example.myexamination.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.myexamination.R;

public class UpdateDialog extends Dialog {
    Context mContext;
    private Button btnSure;
    private Button btnCancel;
    private EditText edtReason;

    public UpdateDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        initView();
    }

    //初始化
    public void initView() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.update_dialog, null);
        btnSure = view.findViewById(R.id.dialog_confirm_sure);
        btnCancel = view.findViewById(R.id.dialog_confirm_cancel);
        edtReason = view.findViewById(R.id.edt_reason);
        super.setContentView(view);
    }

    //获取当前输入框对象
    public View getEditText() {
        return edtReason;
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
