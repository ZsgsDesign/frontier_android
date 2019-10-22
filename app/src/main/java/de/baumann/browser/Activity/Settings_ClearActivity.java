package xyz.johnzhang.frontier.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import xyz.johnzhang.frontier.Fragment.Fragment_clear;
import xyz.johnzhang.frontier.Ninja.R;
import xyz.johnzhang.frontier.Unit.BrowserUnit;
import xyz.johnzhang.frontier.Unit.HelperUnit;
import xyz.johnzhang.frontier.View.NinjaToast;

public class Settings_ClearActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        HelperUnit.applyTheme(this);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_clear()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.clear_menu_done_all:

                final BottomSheetDialog dialog = new BottomSheetDialog(Settings_ClearActivity.this);
                View dialogView = View.inflate(Settings_ClearActivity.this, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_clear);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clear();
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

    private void clear() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean clearCache = sp.getBoolean(getString(R.string.sp_clear_cache), false);
        boolean clearCookie = sp.getBoolean(getString(R.string.sp_clear_cookie), false);
        boolean clearHistory = sp.getBoolean(getString(R.string.sp_clear_history), false);
        boolean clearIndexedDB = sp.getBoolean(("sp_clearIndexedDB"), false);

        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View dialogView = View.inflate(this, R.layout.dialog_progress, null);
        TextView textView = dialogView.findViewById(R.id.dialog_text);
        textView.setText(this.getString(R.string.toast_wait_a_minute));
        dialog.setContentView(dialogView);
        //noinspection ConstantConditions
        dialog.show();

        if (clearCache) {
            BrowserUnit.clearCache(this);
        }
        if (clearCookie) {
            BrowserUnit.clearCookie();
        }
        if (clearHistory) {
            BrowserUnit.clearHistory(this);
        }
        if (clearIndexedDB) {
            BrowserUnit.clearIndexedDB(this);
        }

        dialog.hide();
        dialog.dismiss();
        NinjaToast.show(this, R.string.toast_delete_successful);
    }
}
