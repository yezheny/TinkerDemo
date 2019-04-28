package example.glh.tencenthotfix;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import example.glh.tencenthotfix.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断存储权限，如果没有就申请，用户拒绝就提示某些功能不可用
        setPermission();
        Utils.setBackground(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                loadPatch();
                break;
            case R.id.button2:
                killApp();
                break;
        }
    }

    /**
     * 加载热补丁插件
     */
    public void loadPatch() {
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
    }

    /**
     * 杀死应用加载补丁
     */
    public void killApp() {
        ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void setPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取到存储权限
            } else {
                // Permission Denied
                //拒绝了权限
                //AppContext.showToastText(getResources().getString(R.string.denied_permission));
                //AppContext.showToast(R.string.denied_permission);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}