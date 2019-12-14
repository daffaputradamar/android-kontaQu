package com.daffa.kontaqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.daffa.kontaqu.model.Kontak;
import com.daffa.kontaqu.repository.KontakRepository;
import com.daffa.kontaqu.ui.activity.TambahKontak;
import com.daffa.kontaqu.ui.adapter.KontakAdapter;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView emptyView;
    KontakAdapter kontakAdapter;
    RecyclerView rvKontak;
    FloatingActionButton floatingActionButton;
    List<Kontak> listKontak = new ArrayList<>();

    private KontakRepository kontakRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kontakRepository = new KontakRepository(getApplicationContext());
        rvKontak = findViewById(R.id.rvKontak);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        emptyView = findViewById(R.id.empty_view);

        rvKontak.setLayoutManager(new LinearLayoutManager(this));
        rvKontak.addOnItemTouchListener(new RecyclerTouchListener(this, rvKontak, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Kontak kontak = kontakAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, TambahKontak.class);
                intent.putExtra(INTENT_KONTAK, kontak);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            }
        }));

        updateKontakList();
    }

    private void updateKontakList() {
        kontakRepository.getKontaks().observe(this, new Observer<List<Kontak>>() {
            @Override
            public void onChanged(List<Kontak> kontaks) {
                if (kontaks.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    rvKontak.setVisibility(View.VISIBLE);
                    kontakAdapter = new KontakAdapter(kontaks, MainActivity.this);
                    rvKontak.setAdapter(kontakAdapter);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        rvKontak.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if(data.hasExtra(INTENT_KONTAK)) {
                if(data.hasExtra(INTENT_DELETE)) {
                    kontakRepository.deleteKontak((Kontak) data.getSerializableExtra(INTENT_KONTAK));

                } else {
                    kontakRepository.updateKontak((Kontak) data.getSerializableExtra(INTENT_KONTAK));
                }
            } else {
                String nama = data.getStringExtra(INTENT_NAMA);
                String noTelp = data.getStringExtra(INTENT_NO_TELP);
                String email = data.getStringExtra(INTENT_EMAIL);
                kontakRepository.insertKontak(nama, noTelp, email);
            }
            updateKontakList();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, TambahKontak.class);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    public static interface ClickListener {
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}