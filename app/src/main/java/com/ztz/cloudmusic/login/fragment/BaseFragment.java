package com.ztz.cloudmusic.login.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;


/**
 * 所有Fragment的父类
 * Created by wqewqe on 2017/6/7.
 */

public class BaseFragment extends Fragment{
    ProgressDialog progressDialog;

    /**
     * 显示提示框
     * 提示文本
     * @param msg
     */
    public void showProgressDialog(String msg){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getContext());
        }
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    /**
     * 关闭提示框
     */
    public void closeProgressDialog(){
        if(progressDialog!=null||progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
