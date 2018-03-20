package com.example.kayda.mendle;

        import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar maintoolbar;
    private TabLayout mTabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        maintoolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("Mendle");

        mViewPager=(ViewPager) findViewById(R.id.main_pager);

        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            Intent intent= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            // No user is signed in
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_logout_button){

            logout();

        }
        if(item.getItemId()==R.id.settings_button){

            Intent settingsIntent= new Intent(MainActivity.this,UserActivity.class);
            startActivity(settingsIntent);

        }
        if(item.getItemId()==R.id.action_search_button){

            Intent usersIntent= new Intent(MainActivity.this,OtherUsersActivity.class);
            startActivity(usersIntent);

        }

        return true;
    }

    private void logout() {

        mAuth.signOut();
        Intent intent= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
