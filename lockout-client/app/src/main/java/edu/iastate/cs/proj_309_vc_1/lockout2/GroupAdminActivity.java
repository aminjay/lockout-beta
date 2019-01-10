package edu.iastate.cs.proj_309_vc_1.lockout2;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupAdminActivity extends AppCompatActivity {


    private GroupAdminFragment groupAdminFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadmin);

        groupAdminFragment = new GroupAdminFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.group_frame, groupAdminFragment).commit();


    }
}
