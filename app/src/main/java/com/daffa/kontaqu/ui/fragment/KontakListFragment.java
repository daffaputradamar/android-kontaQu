package com.daffa.kontaqu.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daffa.kontaqu.Application;
import com.daffa.kontaqu.MainActivity;
import com.daffa.kontaqu.R;
import com.daffa.kontaqu.model.Kontak;
import com.daffa.kontaqu.repository.KontakRepository;
import com.daffa.kontaqu.ui.activity.TambahKontak;
import com.daffa.kontaqu.ui.adapter.KontakAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.daffa.kontaqu.util.AppConstants.ACTIVITY_REQUEST_CODE;
import static com.daffa.kontaqu.util.AppConstants.INTENT_DELETE;
import static com.daffa.kontaqu.util.AppConstants.INTENT_EMAIL;
import static com.daffa.kontaqu.util.AppConstants.INTENT_KONTAK;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NAMA;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NO_TELP;

/**
 * A simple {@link Fragment} subclass.
 */
public class KontakListFragment extends Fragment implements View.OnClickListener {

    public static final String SWITCH_KEY = "switch";
    public static final String LIST_KEY = "list";


    TextView emptyView, txtKontakList;
    KontakAdapter kontakAdapter;
    RecyclerView rvKontak;
    FloatingActionButton floatingActionButton;
    List<Kontak> listKontak = new ArrayList<>();
    boolean isList;

    private SharedPreferences preferences;

    private KontakRepository kontakRepository;
    public KontakListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean switchValue = preferences.getBoolean(SWITCH_KEY, false);
        if (switchValue) {
            txtKontakList.setVisibility(View.GONE);
        } else {
            txtKontakList.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kontak_list, container, false);

        isList = (preferences.getString(LIST_KEY, "List")).equals("List") ? true : false;

        kontakRepository = new KontakRepository(getActivity().getApplicationContext());
        rvKontak = view.findViewById(R.id.rvKontak);
        txtKontakList = view.findViewById(R.id.txtKontakList);

        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        emptyView = view.findViewById(R.id.empty_view);

        if (isList) {
            rvKontak.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        } else {
            rvKontak.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        }
        rvKontak.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvKontak, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Kontak kontak = kontakAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), TambahKontak.class);
                intent.putExtra(INTENT_KONTAK, kontak);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            }
        }));

        updateKontakList();

        return view;
    }

    private void updateKontakList() {
        kontakRepository.getKontaks().observe(this, new Observer<List<Kontak>>() {
            @Override
            public void onChanged(List<Kontak> kontaks) {
                if (kontaks.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    rvKontak.setVisibility(View.VISIBLE);
                    kontakAdapter = new KontakAdapter(kontaks, getActivity(), isList);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Intent intent = new Intent(getActivity(), TambahKontak.class);
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
