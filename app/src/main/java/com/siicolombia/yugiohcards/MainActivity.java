package com.siicolombia.yugiohcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.siicolombia.yugiohcards.fragments.fragmentHome.HomeFragment;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, homeFragment).commit();

    }
}