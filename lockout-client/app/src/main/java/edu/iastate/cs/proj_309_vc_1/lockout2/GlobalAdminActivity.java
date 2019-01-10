package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class GlobalAdminActivity extends AppCompatActivity {


    private GlobalAdminFragment globalAdminFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globaladmin);

        globalAdminFragment = new GlobalAdminFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.group_frame_global, globalAdminFragment).commit();


    }
}
