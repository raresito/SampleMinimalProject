package com.example.cristianbaita.sampleminimalproject.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cristianbaita.sampleminimalproject.R;
import com.example.cristianbaita.sampleminimalproject.pojo.interfaces.OnChangeListener;
import com.example.cristianbaita.sampleminimalproject.pojo.views.MainModel;

import org.w3c.dom.Text;

/**
 * Created by cristian.baita on 3/16/2016.
 */
public class MainLayout extends RelativeLayout implements View.OnClickListener, OnChangeListener<MainModel>{
    private static final String TAG = "MainLayout";

    private Context context;
    private MainModel model;

    private Button loginButton;
    private Button fooButton;
    private Button barButton;
    private TextView someText;
    private TextView otherText;

    public interface ViewListener {
        public void onSignInClicked(String username, String password);
        public void onForgotPasswordClicked();
        public void onNeedHelpClicked();
        public void onDebugButtonClicked();
        public void onBackButtonClicked();
        public void onSignUpClicked();
    }

    private ViewListener viewListener;

    public MainLayout(Context context) {
        super(context);
        this.context = context;
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setModel(MainModel mainModel)
    {
        this.model = mainModel;
        this.model.addListener(this);
    }

    public void setViewListener(ViewListener viewListener)
    {
        this.viewListener = viewListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // init all view components
        someText = (TextView) findViewById(R.id.test_text);
        someText.setOnClickListener(this);

    }

    private void updateView()
    {
        someText.setText(model.getUsername());
        // ....
    }

    @Override
    public void onClick(View view) {
        // here should be a switch with all the ID's for all the listened components
        switch (view.getId())
        {
            case R.id.test_text:{
                viewListener.onBackButtonClicked();
                break;
            }
            // ...
        }
    }

    @Override
    public void onChange() {
        updateView();
        // ....
    }



}
