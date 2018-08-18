package com.devilwwj.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.devilwwj.mvp.presenter.UserPresenter;
import com.devilwwj.mvp.view.IUserView;

/**
 * model-处理业务逻辑（主要是数据读写，或者与后台通信（其实也是读写数据）），view-处理ui控件，presenter-主导器，操作model和view
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, IUserView{
    UserPresenter presenter;
    EditText etId, etFirstName, etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.load).setOnClickListener(this);
        etId = (EditText) findViewById(R.id.id);
        etFirstName = (EditText) findViewById(R.id.first);
        etLastName = (EditText) findViewById(R.id.last);

        presenter = new UserPresenter(this);

    }

    @Override
    public int getID() {
        return new Integer(etId.getText().toString());
    }

    @Override
    public String getFirstName() {
        return etFirstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return etLastName.getText().toString();
    }

    @Override
    public void setFirstName(String firstName) {
        etFirstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        etFirstName.setText(lastName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                presenter.saveUser(getID(), getFirstName(), getLastName());
                break;
            case R.id.load:
                presenter.loadUser(getID());
                break;
        }
    }
}
