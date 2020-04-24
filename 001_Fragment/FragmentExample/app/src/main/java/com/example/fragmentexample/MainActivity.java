package com.example.fragmentexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BlankFragment.FragmentListener {


    int count = 0;
    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BlankFragment a = BlankFragment.newInstance("aaaa", "bbbb");
        fragments = new Fragment[3];
        fragments[0] = new RedFragment();
        fragments[1] = new BlueFragment();
        fragments[2] = new BlackFragment();;

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.fragment_container, a);
      //  fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.next:
                count++;
                break;
            case R.id.prev:
                count--;
                break;
        }

        Fragment fragment = fragments[count];
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
              //  .addToBackStack(null)
                .commit();

    }


    @Override
    public void onBtnClick(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
