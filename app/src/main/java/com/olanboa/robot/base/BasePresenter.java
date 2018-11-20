package com.olanboa.robot.base;

import android.content.Context;
import android.widget.Toast;

public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {

    private Context context;
    private V v;
    private M m;

    protected abstract M setModel();

    public M getModel() {
        return m;
    }

    public Context getContext() {
        return context;
    }

    public V getView() {
        return v;
    }

    public BasePresenter(Context context, V v) {
        this.context = context;
        this.m = setModel();
        this.v = v;
    }


    public void showToastInfo(String info) {
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

}
