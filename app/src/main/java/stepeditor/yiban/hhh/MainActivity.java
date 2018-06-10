package stepeditor.yiban.hhh;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tv_step;
    private Button bt_confirm;
    private EditText et_step, et_id;
    private String id, step;
    private CheckBox cb_isChecked;
    private static final int MY_PERMISSION_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        et_step = (EditText) findViewById(R.id.et_step);
        et_id = (EditText) findViewById(R.id.et_id);
        tv_step = (TextView) findViewById(R.id.tv_showStep);
        cb_isChecked = (CheckBox) findViewById(R.id.cb_check);


        String id = WriteDataUtils.readID(this);
        if (!TextUtils.isEmpty(id)) {
            cb_isChecked.setChecked(true);
            et_id.setText(id);
        }
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllGranted = checkPermissionAllGranted(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });

                if (isAllGranted) {
                    doClick();
                    return;
                }else{
                    Toast.makeText(MainActivity.this,"需要授权！",Toast.LENGTH_LONG).show();
                }

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, MY_PERMISSION_REQUEST_CODE);
            }
        });
    }

    public void doClick() {
        id = et_id.getText().toString().trim();
        step = et_step.getText().toString().trim();

        boolean myResult = CheckFileNameForExistance.itExits(id);
        if (!myResult) {
            Toast.makeText(MainActivity.this, "请先登录这个ID对应的账户，并进入易运动等待运动数据刷新！", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, String> maps = WriteDataUtils.readData(id);
        if(maps==null){
            Toast.makeText(MainActivity.this,"读取数据失败！",Toast.LENGTH_LONG).show();
            return;
        }
        String oldStep = maps.get("user_step_total_count");
        int formerStep = Integer.parseInt(oldStep);
        if (formerStep > 99999) {
            Toast.makeText(MainActivity.this, "已经超过10万步了，不会显示在排行榜了", Toast.LENGTH_SHORT).show();

        }

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(step)) {
            Toast.makeText(MainActivity.this, "ID或者步数不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (cb_isChecked.isChecked()) {
                WriteDataUtils.saveID(MainActivity.this, id);
            }

//            System.out.println(id + " " + step);
            boolean result = WriteDataUtils.writeStepintoFile(id, step);

            //System.out.println(result);
            if (result) {
                maps = WriteDataUtils.readData(id);
                if(maps==null){
                    Toast.makeText(MainActivity.this,"读取数据失败！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (Integer.parseInt(maps.get("user_step_total_count")) == 0) {
                    tv_step.setText("增加的步数为：" + ShowInfoUtils.showTotalSteps(maps, step));
                } else {
                    tv_step.setText("修改后的总步数为：" + ShowInfoUtils.showTotalSteps(maps, step));
                }
                Toast.makeText(MainActivity.this, "刷入成功，请重新进入易运动查看！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "写入数据错误，请将机型和安卓版本号反馈至867433846@qq.com", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                doClick();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("需要访问“外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"没有相应权限，请获取",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

}
