package com.smartschool;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.smartschool.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    TextView username;
    TextView userClass;
    DrawerLayout drawerLayout;
    ImageView enterImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkPermissions();

        drawerLayout=(DrawerLayout) findViewById(R.id.container);
        View view=(View) findViewById(R.id.tb);
        Toolbar toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setFitsSystemWindows(true);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        NavigationView navigationView=(NavigationView) findViewById(R.id.drawer_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_quit:
                        LoginActivity.loginSuccess=false;
                        enterImg.setVisibility(View.VISIBLE);
                        Cookies=new HashMap<>();
                        drawerLayout.close();
                        break;
                    case R.id.nav_exit:
                        finish();
                        break;
                    default:break;
                }
                return true;
            }
        });
        View leftHeader=navigationView.getHeaderView(0);
        username=(TextView) leftHeader.findViewById(R.id.name_tv);
        userClass=(TextView) leftHeader.findViewById(R.id.class_tv);
        enterImg=(ImageView) leftHeader.findViewById(R.id.enter_login_img);
        enterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(MainActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        username.setText(BaseActivity.username);
        userClass.setText(BaseActivity.userClass);
        if(LoginActivity.loginSuccess){
            enterImg.setVisibility(View.GONE);

        }else{
            enterImg.setVisibility(View.VISIBLE);
        }
    }

    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(MainActivity.this,"请先允许访问网络！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }
}