package com.la.mylogin.pager.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.la.mylogin.R;
import com.la.mylogin.pager.login.Login;
import com.la.mylogin.pager.login.LoginPresenter;

public class LoginFragment extends Fragment implements Login.View{
// 能一个类完成的不要写两个类，能一个方法做玩的不要写两个方法，能不依赖外部就尽量别依赖外部
    private Login.Presenter presenter;
    private View view;
    private EditText etUsername;
    private EditText etPassword;
    private Button btn_login;
    private Button btn_register;
    private ImageView headImage;
    private CheckBox rememberPassword;
    private ImageButton passwordVisible;
    private Context context;
    private Login.LoginCallback loginCallback;

    public LoginFragment(Login.LoginCallback loginCallback){
        this.loginCallback = loginCallback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 第一个参数是指要加载的布局的id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不想嵌套可以是null
        view = inflater.inflate(R.layout.login_fragemnt, null);
        etUsername = view.findViewById(R.id.login_et_username);
        etPassword = view.findViewById(R.id.login_et_password);
        btn_login = view.findViewById(R.id.login_btn_login);
        btn_register = view.findViewById(R.id.login_btn_register);
        headImage = view.findViewById(R.id.login_head_image);
        rememberPassword = view.findViewById(R.id.login_remember_checkbox);
        passwordVisible = view.findViewById(R.id.login_password_visible);
        presenter = new LoginPresenter(context,this);
        initEditText();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogin(etUsername.getText().toString(),etPassword.getText().toString());
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        passwordVisible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    // 设置可见
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    // 设置不可见
                    etPassword.setInputType(129);
                    return true;
                }
                return false;
            }
        });


        return view;
    }


    // 设置登录中界面
    @Override
    public void logining() {

    }

    // 如果成功，如果记住密码将密码保存到本地，否则就清空密码栏，弹出登录成功提示
    @Override
    public void loginSucceeded() {
        if (rememberPassword.isChecked()){
        // 将密码持久化到本地
            setIsRememberPassword();
        }else {
        // 将密码从本地的持久化中删除
            setIsRememberPassword();
            etPassword.setText("");
        }
        Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
        loginCallback.loginSucceeded(null);
    }

    // 如果失败，直接将密码置空，弹出登录失败提示
    @Override
    public void loginFailed() {
        etPassword.setText("");
        Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
    }

    // 根据密码持久化初始化输入控件
    private void initEditText() {
        SharedPreferences userSet = context.getSharedPreferences(context.getString(R.string.login_app_name), Context.MODE_PRIVATE);
        boolean isRememberPassword = userSet.getBoolean(context.getString(R.string.login_remenber_password),false);
        rememberPassword.setChecked(isRememberPassword);

        if (isRememberPassword){
            etUsername.setText(userSet.getString(context.getString(R.string.login_username),""));
            etPassword.setText(userSet.getString(context.getString(R.string.login_password), ""));
        }else {
            etUsername.setText("");
            etPassword.setText("");
        }
    }
    // 根据是否记住密码设置密码持久化
    private boolean setIsRememberPassword() {
        SharedPreferences userSet = context.getSharedPreferences(context.getString(R.string.login_app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSet.edit();
        if (rememberPassword.isChecked()){
            // 保存需要记住的密码并设置记住状态为是
            editor.putBoolean(context.getString(R.string.login_remenber_password), true);
            editor.putString(context.getString(R.string.login_username),etUsername.getText().toString());
            editor.putString(context.getString(R.string.login_password),etPassword.getText().toString());
            editor.commit();
            return true;
        } else {
            // 删除需要记住的密码
            editor.remove(context.getString(R.string.login_username));
            editor.remove(context.getString(R.string.login_password));
            editor.commit();
            return false;
        }
    }



}
