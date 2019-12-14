package com.daffa.kontaqu.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daffa.kontaqu.R;
import com.daffa.kontaqu.model.Kontak;

import static com.daffa.kontaqu.util.AppConstants.INTENT_DELETE;
import static com.daffa.kontaqu.util.AppConstants.INTENT_EMAIL;
import static com.daffa.kontaqu.util.AppConstants.INTENT_KONTAK;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NAMA;
import static com.daffa.kontaqu.util.AppConstants.INTENT_NO_TELP;

public class TambahKontak extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private EditText editNama, editNoTelp, editEmail;
    private TextView textTime, btnDone, toolbarTitle;
    private ImageView btnDelete, btnPhone, btnMessage;

    private Kontak kontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kontak);

        toolbarTitle = findViewById(R.id.title);
        editNama = findViewById(R.id.editNama);
        editNoTelp = findViewById(R.id.editNoTelp);
        editEmail = findViewById(R.id.editEmail);

        btnDelete = findViewById(R.id.btn_close);
        btnDelete.setOnClickListener(this);

        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        btnPhone = findViewById(R.id.btnPhone);
        btnMessage = findViewById(R.id.btnEmail);

        kontak = (Kontak) getIntent().getSerializableExtra(INTENT_KONTAK);
        if (kontak == null) {
            btnPhone.setVisibility(View.GONE);
            btnMessage.setVisibility(View.GONE);
            toolbarTitle.setText("Tambah Kontak");
            btnDelete.setImageResource(R.drawable.btn_done);
            btnDelete.setTag(R.drawable.btn_done);
        } else {
            toolbarTitle.setText("Kontak Detail");

            btnPhone.setVisibility(View.VISIBLE);
            btnPhone.setOnClickListener(this);

            btnMessage.setVisibility(View.VISIBLE);
            btnMessage.setOnClickListener(this);

            btnDelete.setImageResource(R.drawable.ic_delete);
            btnDelete.setTag(R.drawable.ic_delete);
            if (kontak.getNama() != null && !kontak.getNama().isEmpty()) {
                editNama.setText(kontak.getNama());
                editNama.setSelection(editNama.getText().length());
            }
            if (kontak.getNoTelp() != null && !kontak.getNoTelp().isEmpty()) {
                editNoTelp.setText(kontak.getNoTelp());
                editNoTelp.setSelection(editNoTelp.getText().length());
            }
            if (kontak.getEmail() != null && !kontak.getEmail().isEmpty()) {
                editEmail.setText(kontak.getEmail());
                editEmail.setSelection(editEmail.getText().length());
            }
        }


    }

    @Override
    public void onClick(View view) {
        if (view == btnDelete) {
            if((Integer)btnDelete.getTag() == R.drawable.btn_done) {
                setResult(Activity.RESULT_CANCELED);

            } else {
                Intent intent = getIntent();
                intent.putExtra(INTENT_DELETE, true);
                intent.putExtra(INTENT_KONTAK, kontak);
                setResult(Activity.RESULT_OK, intent);
            }

            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        } else if (view == btnDone) {
            Intent intent = getIntent();
            if(kontak != null) {
                kontak.setNama(editNama.getText().toString());
                kontak.setNoTelp(editNoTelp.getText().toString());
                kontak.setEmail(editEmail.getText().toString());
                intent.putExtra(INTENT_KONTAK, kontak);

            } else {
                intent.putExtra(INTENT_NAMA, editNama.getText().toString());
                intent.putExtra(INTENT_NO_TELP, editNoTelp.getText().toString());
                intent.putExtra(INTENT_EMAIL, editEmail.getText().toString());
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        } else if (view == btnPhone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + kontak.getNoTelp()));
            startActivity(intent);
        } else if (view == btnMessage) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
            intent.putExtra("address", kontak.getNoTelp());
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
