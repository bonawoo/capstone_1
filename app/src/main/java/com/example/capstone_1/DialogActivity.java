package com.example.capstone_1;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

//...중략

public class DialogActivity extends AppCompatActivity {

    private Handler handler;
    private final Context context = this;
    private String phoneNum = "";
    private String textMsg;
    private String prevNumber;
    private SQLiteDatabase sqls;
    private static final String TABLE_NAME = "ContactList";
    private static final String COLUMN_CONTACT  = "contact";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        /*final SMSDBhelper smsdBhelper = new SMSDBhelper(this);
        smsdBhelper.open();*/

        Uri notification = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),notification);
        ringtone.play();
        handler = new Handler();
        dialog = new Dialog(context);

        dialog.setContentView(R.layout.custom_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                dialog.dismiss();
                finish();

            }
        });
        dialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                try {
                    //넘겨받은 위치 정보.
                    double latitude = intent.getExtras().getDouble("lastlat");
                    double longitude = intent.getExtras().getDouble("lastlon");
                    //Cursor cursor = smsdBhelper.getAllContacts();
                    //smsdBhelper.close();
                    //cursor.moveToFirst();
                    //저장된 연락처만큼 전송한다.
                    /*if (cursor.moveToFirst()) {
                        do {
                            String data = cursor.getString(cursor.getColumnIndex("contact"));
                            itemIds.add(data);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();*/
                    phoneNum = "01039336036";
                    int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION);
                    int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.SEND_SMS);
                    Log.e("권한체크", String.valueOf(permission1) + String.valueOf(permission2));
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_DENIED) {
                        //위치 정보를 전송하기 위해 구글 url에 덧붙인다. 참고로 SMS이기 때문에 주소가 너무 길어지면 안 보내지는 상황이 벌어진다...
                        textMsg = "사고 발생!" + "http://maps.google.com/?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude);
                        Toast.makeText(getApplicationContext(), textMsg, Toast.LENGTH_LONG).show();

                        try {
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(phoneNum, null, textMsg, null, null);
                            Toast.makeText(getApplicationContext(), "메세지 전송" + String.valueOf(latitude) + "," + String.valueOf(longitude), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("Message", textMsg + "<" + phoneNum + ">");
                        //이전 번호와 현재 번호를 바꿔준다.
                    }

                    handler.removeCallbacksAndMessages(null);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_SHORT).show();
                }
            }
            //약 7초간 응답이 없을 경우 실행된다.
        }, 7000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
    }

}
