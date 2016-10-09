package cn.modificator.pieface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobvoi.android.common.MobvoiApiManager;
import com.mobvoi.android.common.api.MobvoiApiClient;
import com.mobvoi.android.wearable.Wearable;

public class PieFaceSetting extends AppCompatActivity {
    MobvoiApiClient mobvoiApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_face_setting);
    }
}
