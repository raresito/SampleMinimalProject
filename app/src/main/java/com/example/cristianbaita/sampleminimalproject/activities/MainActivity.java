package com.example.cristianbaita.sampleminimalproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cristianbaita.sampleminimalproject.R;
import com.example.cristianbaita.sampleminimalproject.managers.Manager;
import com.example.cristianbaita.sampleminimalproject.pojo.views.MainModel;
import com.example.cristianbaita.sampleminimalproject.utils.SystemUtils;
import com.example.cristianbaita.sampleminimalproject.views.MainLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MainModel model;
    private MainLayout layout;

    private MainLayout.ViewListener viewListener = new MainLayout.ViewListener() {
        @Override
        public void onSignInClicked(String username, String password) {
            Log.v(TAG, "onSignInClicked with username = " + username);
            Manager.getInstance().getAnalyticsManager().trackEvent();

            if(Manager.getInstance().getDbManager().saveProfile()){
                String tempVar = SystemUtils.getAppVersionName(MainActivity.this);
                // ....
            }
            else{

            }

            // ....
        }

        @Override
        public void onForgotPasswordClicked() {

        }

        @Override
        public void onNeedHelpClicked() {

        }

        @Override
        public void onDebugButtonClicked() {

        }

        @Override
        public void onBackButtonClicked() {

        }

        @Override
        public void onSignUpClicked() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make other checks regarding bundles and receivers
        // set windows params and stuff
        // ...

        init();
    }

    private void init(){
        model = new MainModel();
        layout = (MainLayout) View.inflate(this, R.layout.activity_main, null);
        layout.setViewListener(viewListener);
        layout.setModel(model);

        setContentView(layout);
        // ... other checks
        // here can also be the model set

        model.notifyChanged();
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
