package com.daffa.kontaqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.daffa.kontaqu.model.Kontak;
import com.daffa.kontaqu.repository.KontakRepository;
import com.daffa.kontaqu.ui.activity.TambahKontak;
import com.daffa.kontaqu.ui.adapter.KontakAdapter;
import com.daffa.kontaqu.ui.fragment.KontakListFragment;
import com.daffa.kontaqu.ui.fragment.SettingFragment;
import com.daffa.kontaqu.util.AppConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.daffa.kontaqu.util.AppConstants.ACTIVITY_REQUEST_CODE;
import static com.daffa.kontaqu.util.AppConstants.INTENT_DELETE;
import static com.daffa.kontaqu.util.AppConstants.INTENT_EMAIL;
import static com.daffa.kontaqu.util.AppConstants.INTENT_KONTAK;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NAMA;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NO_TELP;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openFragment(new KontakListFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (fragment == null || fragment instanceof KontakListFragment) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, new SettingFragment())
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFragment(Fragment fragment) {
        openFragment(fragment, false);
    }

    private void openFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackstack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}