package xyz.johnzhang.frontier.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import xyz.johnzhang.frontier.Activity.Settings_ClearActivity;
import xyz.johnzhang.frontier.Activity.Settings_DataActivity;
import xyz.johnzhang.frontier.Activity.Settings_FilterActivity;
import xyz.johnzhang.frontier.Activity.Settings_GestureActivity;
import xyz.johnzhang.frontier.Activity.Settings_StartActivity;
import xyz.johnzhang.frontier.Activity.Settings_UIActivity;
import xyz.johnzhang.frontier.Unit.HelperUnit;
import xyz.johnzhang.frontier.Ninja.R;

public class Fragment_settings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getTitleRes()) {

            case R.string.setting_filter:
                Intent filter = new Intent(getActivity(), Settings_FilterActivity.class);
                getActivity().startActivity(filter);
                break;
            case R.string.setting_title_data:
                Intent dataControl = new Intent(getActivity(), Settings_DataActivity.class);
                getActivity().startActivity(dataControl);
                break;
            case R.string.setting_title_ui:
                Intent uiControl = new Intent(getActivity(), Settings_UIActivity.class);
                getActivity().startActivity(uiControl);
                break;
            case R.string.setting_gestures:
                Intent gestureControl = new Intent(getActivity(), Settings_GestureActivity.class);
                getActivity().startActivity(gestureControl);
                break;
            case R.string.setting_title_start_control:
                Intent startControl = new Intent(getActivity(), Settings_StartActivity.class);
                getActivity().startActivity(startControl);
                break;
            case R.string.setting_title_clear_control:
                Intent clearControl = new Intent(getActivity(), Settings_ClearActivity.class);
                getActivity().startActivity(clearControl);
                break;
            case R.string.setting_title_license:
                showLicenseDialog(getString(R.string.license_title), getString(R.string.license_dialog));
                break;
            case R.string.setting_title_community:
                showLicenseDialog(getString(R.string.setting_title_community), getString(R.string.cont_dialog));
                break;
            case R.string.changelog_title:
                HelperUnit.show_dialogChangelog(getActivity());
                break;
            case R.string.setting_title_appSettings:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getActivity().startActivity(intent);
                break;

            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showLicenseDialog(String title, String text) {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        View dialogView = View.inflate(getActivity(), R.layout.dialog_text, null);

        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        dialog_title.setText(title);

        TextView dialog_text = dialogView.findViewById(R.id.dialog_text);
        dialog_text.setText(HelperUnit.textSpannable(text));
        dialog_text.setMovementMethod(LinkMovementMethod.getInstance());

        ImageButton fab = dialogView.findViewById(R.id.floatButton_ok);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        ImageButton fab_help = dialogView.findViewById(R.id.floatButton_help);
        fab_help.setVisibility(View.GONE);

        ImageButton fab_settings = dialogView.findViewById(R.id.floatButton_settings);
        fab_settings.setVisibility(View.GONE);

        dialog.setContentView(dialogView);
        dialog.show();
        HelperUnit.setBottomSheetBehavior(dialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }
}
