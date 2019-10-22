package xyz.johnzhang.frontier.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import xyz.johnzhang.frontier.Browser.Cookie;
import xyz.johnzhang.frontier.Database.RecordAction;
import xyz.johnzhang.frontier.Ninja.R;
import xyz.johnzhang.frontier.Unit.BrowserUnit;
import xyz.johnzhang.frontier.Unit.HelperUnit;
import xyz.johnzhang.frontier.View.Adapter_Cookie;
import xyz.johnzhang.frontier.View.NinjaToast;

public class Whitelist_Cookie extends AppCompatActivity {
    private Adapter_Cookie adapter;
    private List<String> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        HelperUnit.applyTheme(this);
        setContentView(R.layout.whitelist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecordAction action = new RecordAction(this);
        action.open(false);
        list = action.listDomainsCookie();
        action.close();

        ListView listView = findViewById(R.id.whitelist);
        listView.setEmptyView(findViewById(R.id.whitelist_empty));

        adapter = new Adapter_Cookie(this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button button = findViewById(R.id.whitelist_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.whitelist_edit);
                String domain = editText.getText().toString().trim();
                if (domain.isEmpty()) {
                    NinjaToast.show(Whitelist_Cookie.this, R.string.toast_input_empty);
                } else if (!BrowserUnit.isURL(domain)) {
                    NinjaToast.show(Whitelist_Cookie.this, R.string.toast_invalid_domain);
                } else {
                    RecordAction action = new RecordAction(Whitelist_Cookie.this);
                    action.open(true);
                    if (action.checkDomainCookie(domain)) {
                        NinjaToast.show(Whitelist_Cookie.this, R.string.toast_domain_already_exists);
                    } else {
                        Cookie cookie = new Cookie(Whitelist_Cookie.this);
                        cookie.addDomain(domain.trim());
                        list.add(0, domain.trim());
                        adapter.notifyDataSetChanged();
                        NinjaToast.show(Whitelist_Cookie.this, R.string.toast_add_whitelist_successful);
                    }
                    action.close();
                }
            }
        });
    }

    @Override
    public void onPause() {
        hideSoftInput(findViewById(R.id.whitelist_edit));
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_whitelist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.whitelist_menu_clear:

                final BottomSheetDialog dialog = new BottomSheetDialog(Whitelist_Cookie.this);
                View dialogView = View.inflate(Whitelist_Cookie.this, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_clear);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cookie cookie = new Cookie(Whitelist_Cookie.this);
                        cookie.clearDomains();
                        list.clear();
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.show();

                break;
            default:
                break;
        }
        return true;
    }

    private void hideSoftInput(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
