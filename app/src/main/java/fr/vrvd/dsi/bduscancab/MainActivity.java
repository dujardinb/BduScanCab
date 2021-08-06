package fr.vrvd.dsi.bduscancab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.vrvd.dsi.bduscancablibrary.scanQR;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btScan = findViewById(R.id.btScan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, scanQR.class);
                intent.putExtra("FORMAT_CAB",getResources().getInteger(R.integer.FORMAT_QR_CODE));
                intent.putExtra("VIBRATE",1);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView tvscan = findViewById(R.id.tvScan);
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                tvscan.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                tvscan.setText("");
            }
        }
    }
}