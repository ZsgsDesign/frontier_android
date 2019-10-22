package xyz.johnzhang.frontier.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.graphics.Palette;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mobapphome.mahencryptorlib.MAHEncryptor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import xyz.johnzhang.frontier.Browser.AdBlock;
import xyz.johnzhang.frontier.Browser.AlbumController;
import xyz.johnzhang.frontier.Browser.BrowserContainer;
import xyz.johnzhang.frontier.Browser.BrowserController;
import xyz.johnzhang.frontier.Browser.Cookie;
import xyz.johnzhang.frontier.Browser.Javascript;
import xyz.johnzhang.frontier.Database.BookmarkList;
import xyz.johnzhang.frontier.Database.Record;
import xyz.johnzhang.frontier.Database.RecordAction;
import xyz.johnzhang.frontier.Ninja.R;
import xyz.johnzhang.frontier.Service.ClearService;
import xyz.johnzhang.frontier.Service.HolderService;
import xyz.johnzhang.frontier.Task.ScreenshotTask;
import xyz.johnzhang.frontier.Unit.BrowserUnit;
import xyz.johnzhang.frontier.Unit.HelperUnit;
import xyz.johnzhang.frontier.Unit.IntentUnit;
import xyz.johnzhang.frontier.Unit.ViewUnit;
import xyz.johnzhang.frontier.View.CompleteAdapter;
import xyz.johnzhang.frontier.View.FullscreenHolder;
import xyz.johnzhang.frontier.View.GridAdapter;
import xyz.johnzhang.frontier.View.GridAdapter_filter;
import xyz.johnzhang.frontier.View.GridItem;

import xyz.johnzhang.frontier.View.GridItem_filter;
import xyz.johnzhang.frontier.View.NinjaToast;
import xyz.johnzhang.frontier.View.NinjaWebView;
import xyz.johnzhang.frontier.View.Adapter_Record;
import xyz.johnzhang.frontier.View.SwipeTouchListener;

import static android.content.ContentValues.TAG;

@SuppressWarnings({"ResultOfMethodCallIgnored", "FieldCanBeLocal", "ApplySharedPref"})
public class BrowserActivity extends AppCompatActivity implements BrowserController, View.OnClickListener {

    // Menus

    private LinearLayout menu_tabPreview;
    private LinearLayout menu_newTabOpen;
    private LinearLayout menu_closeTab;
    private LinearLayout menu_quit;

    private LinearLayout menu_shareScreenshot;
    private LinearLayout menu_shareLink;
    private LinearLayout menu_sharePDF;
    private LinearLayout menu_openWith;

    private LinearLayout menu_searchSite;
    private LinearLayout menu_settings;
    private LinearLayout menu_download;
    private LinearLayout menu_saveScreenshot;
    private LinearLayout menu_saveBookmark;
    private LinearLayout menu_savePDF;
    private LinearLayout menu_saveStart;
    private LinearLayout menu_help;

    private LinearLayout menu_fav;
    private LinearLayout menu_sc;
    private LinearLayout menu_openFav;
    private LinearLayout menu_shareCLipboard;

    private View floatButton_tabView;
    private View floatButton_saveView;
    private View floatButton_shareView;
    private View floatButton_moreView;

    private ImageButton fab_tab;
    private ImageButton fab_share;
    private ImageButton fab_save;
    private ImageButton fab_more;
    private ImageButton tab_plus;
    private ImageButton tab_plus_bottom;

    // Views

    private ImageButton searchUp;
    private ImageButton searchDown;
    private ImageButton searchCancel;
    private ImageButton omniboxRefresh;
    private ImageButton omniboxOverflow;
    private ImageButton omniboxOverview;

    private ImageButton open_startPage;
    private ImageButton open_bookmark;
    private ImageButton open_history;
    private ImageButton open_menu;

    private FloatingActionButton fab_imageButtonNav;
    private AutoCompleteTextView inputBox;
    private ProgressBar progressBar;
    private EditText searchBox;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog_OverView;
    private NinjaWebView ninjaWebView;
    private ListView listView;
    private TextView omniboxTitle;
    private TextView dialogTitle;
    private View customView;
    private VideoView videoView;

    private HorizontalScrollView tab_ScrollView;
    private LinearLayout overview_top;
    private LinearLayout overview_topButtons;

    // Layouts

    private RelativeLayout appBar;
    private RelativeLayout omnibox;
    private RelativeLayout searchPanel;
    private RelativeLayout activity_main;
    private FrameLayout contentFrame;
    private LinearLayout tab_container;
    private FrameLayout fullscreenHolder;

    private View open_startPageView;
    private View open_bookmarkView;
    private View open_historyView;

    private View overview_titleIcons_startView;
    private View overview_titleIcons_bookmarksView;
    private View overview_titleIcons_historyView;

    // Others

    private String title;
    private String url;
    private String overViewTab;
    private BroadcastReceiver downloadReceiver;
    private BottomSheetBehavior mBehavior;


    private Activity activity;
    private Context context;
    private SharedPreferences sp;
    private MAHEncryptor mahEncryptor;
    private Javascript javaHosts;
    private Javascript getJavaHosts() {
        return javaHosts;
    }
    private Cookie cookieHosts;
    private Cookie getCookieHosts () {return cookieHosts;}
    private AdBlock adBlock;
    private AdBlock getAdBlock() {
        return adBlock;
    }
    private GridAdapter gridAdapter;

    private boolean prepareRecord() {
        NinjaWebView webView = (NinjaWebView) currentAlbumController;
        String title = webView.getTitle();
        String url = webView.getUrl();
        return (title == null
                || title.isEmpty()
                || url == null
                || url.isEmpty()
                || url.startsWith(BrowserUnit.URL_SCHEME_ABOUT)
                || url.startsWith(BrowserUnit.URL_SCHEME_MAIL_TO)
                || url.startsWith(BrowserUnit.URL_SCHEME_INTENT));
    }

    private int originalOrientation;
    private int shortAnimTime;
    private int vibrantDarkColor;
    private int vibrantColor;

    private float dimen156dp;
    private float dimen144dp;
    private float dimen117dp;
    private float dimen108dp;

    private static final float[] NEGATIVE_COLOR = {
            -1.0f, 0, 0, 0, 255, // Red
            0, -1.0f, 0, 0, 255, // Green
            0, 0, -1.0f, 0, 255, // Blue
            0, 0, 0, 1.0f, 0     // Alpha
    };

    private WebChromeClient.CustomViewCallback customViewCallback;
    private ValueCallback<Uri[]> filePathCallback = null;
    private AlbumController currentAlbumController = null;

    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    // Classes

    private class VideoCompletionListener implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
        @Override
        public void onCompletion(MediaPlayer mp) {
            onHideCustomView();
        }
    }

    // Overrides

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        WebView.enableSlowWholeDocumentDraw();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        context = BrowserActivity.this;
        activity = BrowserActivity.this;

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("restart_changed", 0).apply();
        sp.edit().putInt("vibrantColor", getResources().getColor(R.color.colorAccent)).commit();

        HelperUnit.applyTheme(context);
        setContentView(R.layout.activity_main);

        if (Objects.requireNonNull(sp.getString("saved_key_ok", "no")).equals("no")) {
            char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!§$%&/()=?;:_-.,+#*<>".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 25; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            if (Locale.getDefault().getCountry().equals("CN")) {
                sp.edit().putString(getString(R.string.sp_search_engine), "2").apply();
            }
            sp.edit().putString("saved_key", sb.toString()).apply();
            sp.edit().putString("saved_key_ok", "yes").apply();

            sp.edit().putString("setting_gesture_tb_up", "08").apply();
            sp.edit().putString("setting_gesture_tb_down", "01").apply();
            sp.edit().putString("setting_gesture_tb_left", "07").apply();
            sp.edit().putString("setting_gesture_tb_right", "06").apply();

            sp.edit().putString("setting_gesture_nav_up", "04").apply();
            sp.edit().putString("setting_gesture_nav_down", "05").apply();
            sp.edit().putString("setting_gesture_nav_left", "03").apply();
            sp.edit().putString("setting_gesture_nav_right", "02").apply();

            sp.edit().putBoolean(getString(R.string.sp_location), false).apply();
        }

        try {
            mahEncryptor = MAHEncryptor.newInstance(Objects.requireNonNull(sp.getString("saved_key", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentFrame = findViewById(R.id.main_content);
        appBar = findViewById(R.id.appBar);

        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        dimen156dp = getResources().getDimensionPixelSize(R.dimen.layout_width_156dp);
        dimen144dp = getResources().getDimensionPixelSize(R.dimen.layout_width_144dp);
        dimen117dp = getResources().getDimensionPixelSize(R.dimen.layout_height_117dp);
        dimen108dp = getResources().getDimensionPixelSize(R.dimen.layout_height_108dp);

        // Load default colors
        vibrantColor = ContextCompat.getColor(context, R.color.colorAccent);
        vibrantDarkColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        ninjaWebView = (NinjaWebView) currentAlbumController;

        initOmnibox();
        initSearchPanel();
        initOverview();

        new AdBlock(context); // For AdBlock cold boot
        new Javascript(context);
        new Cookie(context);

        // show changelog

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final String versionName = pInfo.versionName;
            String oldVersionName = sp.getString("oldVersionName", "0.0");
            if (!Objects.requireNonNull(oldVersionName).equals(versionName)) {
                HelperUnit.show_dialogChangelog(activity);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        downloadReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_downloadComplete);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                        hideBottomSheetDialog ();
                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideBottomSheetDialog ();
                    }
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
        dispatchIntent(getIntent());

        if (sp.getBoolean("start_tabStart", false)){
            showOverview();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, shortAnimTime);
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] results = null;
        // Check that the response is a good one
        if(resultCode == Activity.RESULT_OK) {
            if(data == null) {
                // If there is not data, then we may have taken a photo
                if(mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentUnit.setContext(context);
        dispatchIntent(getIntent());

        if (sp.getBoolean("pdf_create", false)) {

            bottomSheetDialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);

            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    hideBottomSheetDialog ();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideBottomSheetDialog ();
                }
            });
            bottomSheetDialog.setContentView(dialogView);

            final File pathFile = new File(sp.getString("pdf_path", ""));

            if (sp.getBoolean("pdf_share", false)) {

                if (pathFile.exists() && !sp.getBoolean("pdf_delete", false)) {
                    sp.edit().putBoolean("pdf_delete", true).commit();
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, pathFile.getName());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, pathFile.getName());
                    sharingIntent.setType("*/pdf");
                    Uri bmpUri = Uri.fromFile(pathFile);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.menu_share)));
                } else if (pathFile.exists() && sp.getBoolean("pdf_delete", false)){
                    pathFile.delete();
                    sp.edit().putBoolean("pdf_create", false).commit();
                    sp.edit().putBoolean("pdf_share", false).commit();
                    sp.edit().putBoolean("pdf_delete", false).commit();
                } else {
                    sp.edit().putBoolean("pdf_create", false).commit();
                    sp.edit().putBoolean("pdf_share", false).commit();
                    sp.edit().putBoolean("pdf_delete", false).commit();
                    textView.setText(R.string.menu_share_pdfToast);
                    bottomSheetDialog.show();
                    HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                }

            } else {
                textView.setText(R.string.toast_downloadComplete);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                sp.edit().putBoolean("pdf_share", false).commit();
                sp.edit().putBoolean("pdf_create", false).commit();
                sp.edit().putBoolean("pdf_delete", false).commit();
            }
        }

        if (sp.getBoolean("delete_screenshot", false)) {
            File pathFile = new File(sp.getString("screenshot_path", ""));
            if (pathFile.exists()) {
                pathFile.delete();
                sp.edit().putBoolean("delete_screenshot", false).commit();
            }
        }
    }

    @Override
    public void onPause() {
        Intent toHolderService = new Intent(context, HolderService.class);
        IntentUnit.setClear(false);
        stopService(toHolderService);
        inputBox.clearFocus();
        IntentUnit.setContext(context);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        boolean clearIndexedDB = sp.getBoolean(("sp_clearIndexedDB"), false);
        if (clearIndexedDB) {
            BrowserUnit.clearIndexedDB(context);
        }

        Intent toHolderService = new Intent(this, HolderService.class);
        IntentUnit.setClear(true);
        stopService(toHolderService);

        if (sp.getBoolean(getString(R.string.sp_clear_quit), false)) {
            Intent toClearService = new Intent(this, ClearService.class);
            startService(toClearService);
        }

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File myDir = new File(storageDir + "/FOSS_Browser_temp");
        BrowserUnit.deleteDir(myDir);

        BrowserContainer.clear();
        IntentUnit.setContext(null);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            
            case KeyEvent.KEYCODE_MENU:
                return showOverflow();
            case KeyEvent.KEYCODE_BACK:
                hideKeyboard(activity);
                hideOverview();
                if (fullscreenHolder != null || customView != null || videoView != null) {
                    return onHideCustomView();
                } else if (omnibox.getVisibility() == View.GONE && sp.getBoolean("sp_toolbarShow", true)) {
                    showOmnibox();
                } else {
                    if (ninjaWebView.canGoBack()) {
                        ninjaWebView.goBack();
                    } else {
                        removeAlbum(currentAlbumController);
                    }
                }
                return true;
        }
        return false;
    }

    private void setColor () {

        if (sp.getBoolean("sp_themeColor", true) && ninjaWebView == currentAlbumController){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            int width = size.x;
            final Bitmap icon = ViewUnit.capture(ninjaWebView, width, 112, Bitmap.Config.ARGB_8888);

            Palette.from(icon).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    final Palette.Swatch vibrantDark = p.getDarkVibrantSwatch();
                    final Palette.Swatch vibrant = p.getVibrantSwatch();

                    if(vibrant != null){
                        vibrantColor = vibrant.getRgb();
                    } else {
                        vibrantColor = ContextCompat.getColor(context, R.color.colorAccent);
                    }

                    if(vibrantDark != null  && isDarkColor(vibrantDark.getRgb())){
                        vibrantDarkColor = vibrantDark.getRgb();
                    } else {
                        vibrantDarkColor = ContextCompat.getColor(context, R.color.colorPrimary);
                    }

                    sp.edit().putInt("vibrantColor", vibrantColor).commit();

                    getWindow().setStatusBarColor(vibrantDarkColor);
                    omnibox.setBackgroundColor(vibrantDarkColor);
                    omniboxTitle.setBackgroundColor(vibrantDarkColor);
                    inputBox.setBackgroundColor(vibrantDarkColor);
                    fab_imageButtonNav.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
                }
            });
        }
    }

    @Override
    public synchronized void showAlbum(AlbumController controller) {

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            final View av = (View) controller;

            contentFrame.removeAllViews();
            contentFrame.addView(av);
        } else {
            contentFrame.removeAllViews();
            contentFrame.addView((View) controller);
        }

        currentAlbumController = controller;
        currentAlbumController.activate();
        updateOmnibox();
    }

    @Override
    public void updateAutoComplete() {

        RecordAction action = new RecordAction(this);
        action.open(false);
        List<Record> list = action.listHistory();
        list.addAll(action.listHistory());
        action.close();

        CompleteAdapter adapter = new CompleteAdapter(this, R.layout.complete_item, list);
        inputBox.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        inputBox.setThreshold(1);
        inputBox.setDropDownVerticalOffset(-16);
        inputBox.setDropDownWidth(ViewUnit.getWindowWidth(this));
        inputBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((TextView) view.findViewById(R.id.complete_item_url)).getText().toString();
                updateAlbum(url);
                hideKeyboard(activity);
            }
        });
    }

    @Override
    public void updateBookmarks() {
        RecordAction action = new RecordAction(context);
        action.open(false);
        action.close();
    }

    private void showOverview() {

        overview_top.setVisibility(View.VISIBLE);
        overview_topButtons.setVisibility(View.VISIBLE);
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            currentAlbumController.activate();
        }

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            currentAlbumController.activate();
        }

        open_startPageView.setBackgroundColor(vibrantColor);
        open_historyView.setBackgroundColor(vibrantColor);
        open_bookmarkView.setBackgroundColor(vibrantColor);
        overview_titleIcons_startView.setBackgroundColor(vibrantColor);
        overview_titleIcons_bookmarksView.setBackgroundColor(vibrantColor);
        overview_titleIcons_historyView.setBackgroundColor(vibrantColor);

        bottomSheetDialog_OverView.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                tab_ScrollView.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
            }
        }, shortAnimTime);
    }

    public void hideOverview () {
        if (bottomSheetDialog_OverView != null) {
            bottomSheetDialog_OverView.cancel();
        }
    }

    private void hideBottomSheetDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.cancel();
        }
    }

    @Override
    public void onClick(View v) {

        RecordAction action = new RecordAction(context);
        ninjaWebView = (NinjaWebView) currentAlbumController;

        try {
            title = ninjaWebView.getTitle().trim();
            url = ninjaWebView.getUrl().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (v.getId()) {

            // Menu overflow

            case R.id.tab_plus:
                hideBottomSheetDialog();
                hideOverview();
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"), true);
                break;
            case R.id.tab_plus_bottom:
                hideBottomSheetDialog();
                hideOverview();
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"), true);
                break;

            case R.id.menu_newTabOpen:
                hideBottomSheetDialog();
                hideOverview();
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"), true);
                break;

            case R.id.menu_closeTab:
                hideBottomSheetDialog ();
                removeAlbum(currentAlbumController);
                break;

            case R.id.menu_tabPreview:
                hideBottomSheetDialog ();
                showOverview();
                break;

            case R.id.menu_quit:
                hideBottomSheetDialog ();
                doubleTapsQuit();
                break;

            case R.id.menu_shareScreenshot:
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                        HelperUnit.grantPermissionsStorage(activity);
                    } else {
                        hideBottomSheetDialog ();
                        sp.edit().putInt("screenshot", 1).apply();
                        new ScreenshotTask(context, ninjaWebView).execute();
                    }
                } else {
                    hideBottomSheetDialog ();
                    sp.edit().putInt("screenshot", 1).apply();
                    new ScreenshotTask(context, ninjaWebView).execute();
                }
                break;

            case R.id.menu_shareLink:
                hideBottomSheetDialog ();
                if (prepareRecord()) {
                    NinjaToast.show(context, getString(R.string.toast_share_failed));
                } else {
                    IntentUnit.share(context, title, url);
                }
                break;

            case R.id.menu_sharePDF:
                hideBottomSheetDialog ();
                printPDF(true);
                break;

            case R.id.menu_openWith:
                hideBottomSheetDialog ();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                Intent chooser = Intent.createChooser(intent, getString(R.string.menu_open_with));
                startActivity(chooser);
                break;

            case R.id.menu_saveScreenshot:
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                        HelperUnit.grantPermissionsStorage(activity);
                    } else {
                        hideBottomSheetDialog ();
                        sp.edit().putInt("screenshot", 0).apply();
                        new ScreenshotTask(context, ninjaWebView).execute();
                    }
                } else {
                    hideBottomSheetDialog ();
                    sp.edit().putInt("screenshot", 0).apply();
                    new ScreenshotTask(context, ninjaWebView).execute();
                }
                break;

            case R.id.menu_saveBookmark:
                hideBottomSheetDialog ();
                try {

                    MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(Objects.requireNonNull(sp.getString("saved_key", "")));
                    String encrypted_userName = mahEncryptor.encode("");
                    String encrypted_userPW = mahEncryptor.encode("");

                    BookmarkList db = new BookmarkList(context);
                    db.open();
                    if (db.isExist(url)){
                        NinjaToast.show(context, R.string.toast_newTitle);
                    } else {
                        db.insert(HelperUnit.secString(ninjaWebView.getTitle()), url, encrypted_userName, encrypted_userPW, "01");
                        NinjaToast.show(context, R.string.toast_edit_successful);
                        initBookmarkList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NinjaToast.show(context, R.string.toast_error);
                }
                break;

            case R.id.menu_saveStart:
                hideBottomSheetDialog ();
                action.open(true);
                if (action.checkGridItem(url)) {
                    NinjaToast.show(context, getString(R.string.toast_already_exist_in_home));
                } else {

                    int counter = sp.getInt("counter", 0);
                    counter = counter + 1;
                    sp.edit().putInt("counter", counter).commit();

                    Bitmap bitmap = ViewUnit.capture(ninjaWebView, dimen156dp, dimen117dp, Bitmap.Config.ARGB_8888);
                    String filename = counter + BrowserUnit.SUFFIX_PNG;

                    GridItem itemAlbum = new GridItem(title, url, filename, counter);

                    if (BrowserUnit.bitmap2File(context, bitmap, filename) && action.addGridItem(itemAlbum)) {
                        NinjaToast.show(context, getString(R.string.toast_add_to_home_successful));
                    } else {
                        NinjaToast.show(context, getString(R.string.toast_add_to_home_failed));
                    }
                }
                action.close();
                break;

                // Omnibox

            case R.id.menu_searchSite:
                hideBottomSheetDialog ();
                hideKeyboard(activity);
                showSearchPanel();
                break;

            case R.id.contextLink_saveAs:
                hideBottomSheetDialog ();
                printPDF(false);
                break;

            case R.id.menu_settings:
                hideBottomSheetDialog ();
                Intent settings = new Intent(BrowserActivity.this, Settings_Activity.class);
                startActivity(settings);
                break;

            case R.id.menu_help:
                hideBottomSheetDialog();
                HelperUnit.show_dialogHelp(context);
                break;

            case R.id.menu_download:
                hideBottomSheetDialog ();
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                break;

            case R.id.floatButton_tab:
                menu_newTabOpen.setVisibility(View.VISIBLE);
                menu_closeTab.setVisibility(View.VISIBLE);
                menu_tabPreview.setVisibility(View.VISIBLE);
                menu_quit.setVisibility(View.VISIBLE);

                menu_shareScreenshot.setVisibility(View.GONE);
                menu_shareLink.setVisibility(View.GONE);
                menu_sharePDF.setVisibility(View.GONE);
                menu_openWith.setVisibility(View.GONE);

                menu_saveScreenshot.setVisibility(View.GONE);
                menu_saveBookmark.setVisibility(View.GONE);
                menu_savePDF.setVisibility(View.GONE);
                menu_saveStart.setVisibility(View.GONE);

                floatButton_tabView.setVisibility(View.VISIBLE);
                floatButton_saveView.setVisibility(View.INVISIBLE);
                floatButton_shareView.setVisibility(View.INVISIBLE);
                floatButton_moreView.setVisibility(View.INVISIBLE);

                menu_searchSite.setVisibility(View.GONE);
                menu_help.setVisibility(View.GONE);
                menu_settings.setVisibility(View.GONE);
                menu_download.setVisibility(View.GONE);

                menu_fav.setVisibility(View.GONE);
                menu_sc.setVisibility(View.GONE);
                menu_openFav.setVisibility(View.VISIBLE);
                menu_shareCLipboard.setVisibility(View.GONE);

                break;

            case R.id.floatButton_share:
                menu_newTabOpen.setVisibility(View.GONE);
                menu_closeTab.setVisibility(View.GONE);
                menu_tabPreview.setVisibility(View.GONE);
                menu_quit.setVisibility(View.GONE);

                menu_shareScreenshot.setVisibility(View.VISIBLE);
                menu_shareLink.setVisibility(View.VISIBLE);
                menu_sharePDF.setVisibility(View.VISIBLE);
                menu_openWith.setVisibility(View.VISIBLE);

                menu_saveScreenshot.setVisibility(View.GONE);
                menu_saveBookmark.setVisibility(View.GONE);
                menu_savePDF.setVisibility(View.GONE);
                menu_saveStart.setVisibility(View.GONE);

                floatButton_tabView.setVisibility(View.INVISIBLE);
                floatButton_saveView.setVisibility(View.INVISIBLE);
                floatButton_shareView.setVisibility(View.VISIBLE);
                floatButton_moreView.setVisibility(View.INVISIBLE);

                menu_searchSite.setVisibility(View.GONE);
                menu_help.setVisibility(View.GONE);
                menu_settings.setVisibility(View.GONE);
                menu_download.setVisibility(View.GONE);

                menu_fav.setVisibility(View.GONE);
                menu_sc.setVisibility(View.GONE);
                menu_openFav.setVisibility(View.GONE);
                menu_shareCLipboard.setVisibility(View.VISIBLE);
                break;

            case R.id.floatButton_save:
                menu_newTabOpen.setVisibility(View.GONE);
                menu_closeTab.setVisibility(View.GONE);
                menu_tabPreview.setVisibility(View.GONE);
                menu_quit.setVisibility(View.GONE);

                menu_shareScreenshot.setVisibility(View.GONE);
                menu_shareLink.setVisibility(View.GONE);
                menu_sharePDF.setVisibility(View.GONE);
                menu_openWith.setVisibility(View.GONE);

                menu_saveScreenshot.setVisibility(View.VISIBLE);
                menu_saveBookmark.setVisibility(View.VISIBLE);
                menu_savePDF.setVisibility(View.VISIBLE);
                menu_saveStart.setVisibility(View.VISIBLE);

                menu_searchSite.setVisibility(View.GONE);
                menu_help.setVisibility(View.GONE);

                floatButton_tabView.setVisibility(View.INVISIBLE);
                floatButton_saveView.setVisibility(View.VISIBLE);
                floatButton_shareView.setVisibility(View.INVISIBLE);
                floatButton_moreView.setVisibility(View.INVISIBLE);

                menu_settings.setVisibility(View.GONE);
                menu_download.setVisibility(View.GONE);

                menu_fav.setVisibility(View.GONE);
                menu_sc.setVisibility(View.VISIBLE);
                menu_openFav.setVisibility(View.GONE);
                menu_shareCLipboard.setVisibility(View.GONE);
                break;

            case R.id.floatButton_more:
                menu_newTabOpen.setVisibility(View.GONE);
                menu_closeTab.setVisibility(View.GONE);
                menu_tabPreview.setVisibility(View.GONE);
                menu_quit.setVisibility(View.GONE);

                menu_shareScreenshot.setVisibility(View.GONE);
                menu_shareLink.setVisibility(View.GONE);
                menu_sharePDF.setVisibility(View.GONE);
                menu_openWith.setVisibility(View.GONE);

                menu_saveScreenshot.setVisibility(View.GONE);
                menu_saveBookmark.setVisibility(View.GONE);
                menu_savePDF.setVisibility(View.GONE);
                menu_saveStart.setVisibility(View.GONE);

                floatButton_tabView.setVisibility(View.INVISIBLE);
                floatButton_saveView.setVisibility(View.INVISIBLE);
                floatButton_shareView.setVisibility(View.INVISIBLE);
                floatButton_moreView.setVisibility(View.VISIBLE);

                menu_settings.setVisibility(View.VISIBLE);
                menu_searchSite.setVisibility(View.VISIBLE);
                menu_help.setVisibility(View.VISIBLE);
                menu_download.setVisibility(View.VISIBLE);

                menu_fav.setVisibility(View.VISIBLE);
                menu_sc.setVisibility(View.GONE);
                menu_openFav.setVisibility(View.GONE);
                menu_shareCLipboard.setVisibility(View.GONE);

                break;

            // Buttons

            case R.id.omnibox_overview:
                showOverview();
                break;

            case R.id.omnibox_refresh:
                if (url != null && ninjaWebView.isLoadFinish()) {

                    if (!url.startsWith("https://")) {
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView = View.inflate(context, R.layout.dialog_action, null);
                        TextView textView = dialogView.findViewById(R.id.dialog_text);
                        textView.setText(R.string.toast_unsecured);
                        Button action_ok = dialogView.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                                ninjaWebView.loadUrl(url.replace("http://", "https://"));
                            }
                        });
                        Button action_cancel2 = dialogView.findViewById(R.id.action_cancel);
                        action_cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                                ninjaWebView.reload();
                            }
                        });
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        ninjaWebView.reload();
                    }
                } else if (url == null ){
                    String text = getString(R.string.toast_load_error) + ": " + url;
                    NinjaToast.show(context, text);
                } else {
                    ninjaWebView.stopLoading();

                }
                break;

            default:
                break;
        }
    }

    // Methods

    private void printPDF (boolean share) {

        try {
            sp.edit().putBoolean("pdf_create", true).commit();

            if (share) {
                sp.edit().putBoolean("pdf_share", true).commit();
            } else {
                sp.edit().putBoolean("pdf_share", false).commit();
            }

            String title = HelperUnit.fileName(ninjaWebView.getUrl());
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File file = new File(dir, title + ".pdf");
            sp.edit().putString("pdf_path", file.getPath()).apply();

            String pdfTitle = file.getName().replace(".pdf", "");

            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = ninjaWebView.createPrintDocumentAdapter(title);
            Objects.requireNonNull(printManager).print(pdfTitle, printAdapter, new PrintAttributes.Builder().build());

        } catch (Exception e) {
            sp.edit().putBoolean("pdf_create", false).commit();
            e.printStackTrace();
        }
    }


    private void dispatchIntent(Intent intent) {
        Intent toHolderService = new Intent(context, HolderService.class);
        IntentUnit.setClear(false);
        stopService(toHolderService);

        String action = intent.getAction();

        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_WEB_SEARCH)) {
            // From ActionMode and some others
            pinAlbums(intent.getStringExtra(SearchManager.QUERY));
        } else if (filePathCallback != null) {
            filePathCallback = null;
        } else if ("sc_history".equals(action)) {
            pinAlbums(null);
            showOverview();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    open_history.performClick();
                }
            }, shortAnimTime);
        } else if ("sc_bookmark".equals(action)) {
            pinAlbums(null);
            showOverview();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    open_bookmark.performClick();
                }
            }, shortAnimTime);
        } else if ("sc_startPage".equals(action)) {
            pinAlbums(null);
            showOverview();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    open_startPage.performClick();
                }
            }, shortAnimTime);
        } else if (Intent.ACTION_SEND.equals(action)) {
            pinAlbums(intent.getStringExtra(Intent.EXTRA_TEXT));
        } else if ("".equals(action)) {
            Log.i(TAG, "resumed Frontier Browser");
        } else {
            pinAlbums(null);
        }
        getIntent().setAction("");
    }

    private void initRendering(View view) {
        if (sp.getBoolean("sp_invert", false)) {
            Paint paint = new Paint();
            ColorMatrix matrix = new ColorMatrix();
            matrix.set(NEGATIVE_COLOR);
            ColorMatrix gcm = new ColorMatrix();
            gcm.setSaturation(0);
            ColorMatrix concat = new ColorMatrix();
            concat.setConcat(matrix, gcm);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(concat);
            paint.setColorFilter(filter);
            // maybe sometime LAYER_TYPE_NONE would better?
            view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        } else {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOmnibox() {

        omnibox = findViewById(R.id.main_omnibox);
        inputBox = findViewById(R.id.main_omnibox_input);
        omniboxRefresh = findViewById(R.id.omnibox_refresh);
        omniboxOverview = findViewById(R.id.omnibox_overview);
        omniboxOverflow = findViewById(R.id.omnibox_overflow);
        omniboxTitle = findViewById(R.id.omnibox_title);
        progressBar = findViewById(R.id.main_progress_bar);

        int fab_position = Integer.parseInt(Objects.requireNonNull(sp.getString("nav_position", "0")));

        switch (fab_position) {
            case 0:
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_right);
                break;
            case 1:
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_left);
                break;
            case 2:
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_center);
                break;
            default:
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_right);
                break;
        }

        fab_imageButtonNav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFastToggle();
                return false;
            }
        });

        omniboxOverflow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFastToggle();
                return false;
            }
        });

        fab_imageButtonNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverflow();
            }
        });

        omniboxOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverflow();
            }
        });

        if (sp.getBoolean("sp_gestures_use", true)) {
            fab_imageButtonNav.setOnTouchListener(new SwipeTouchListener(context) {
                public void onSwipeTop() { performGesture("setting_gesture_nav_up"); }
                public void onSwipeBottom() { performGesture("setting_gesture_nav_down"); }
                public void onSwipeRight() { performGesture("setting_gesture_nav_right"); }
                public void onSwipeLeft() { performGesture("setting_gesture_nav_left"); }
            });

            inputBox.setOnTouchListener(new SwipeTouchListener(context) {
                public void onSwipeTop() { performGesture("setting_gesture_tb_up"); }
                public void onSwipeBottom() { performGesture("setting_gesture_tb_down"); }
                public void onSwipeRight() { performGesture("setting_gesture_tb_right"); }
                public void onSwipeLeft() { performGesture("setting_gesture_tb_left"); }
            });
        }

        inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String query = inputBox.getText().toString().trim();
                if (query.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return true;
                }
                updateAlbum(query);
                hideKeyboard(activity);
                showOmnibox();
                return false;
            }
        });

        inputBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (inputBox.hasFocus()) {
                    ninjaWebView.stopLoading();
                    omniboxTitle.setVisibility(View.GONE);
                    inputBox.setText(ninjaWebView.getUrl());
                    inputBox.setSelection(0,inputBox.getText().toString().length());
                } else {
                    omniboxTitle.setVisibility(View.VISIBLE);
                    omniboxTitle.setText(ninjaWebView.getTitle());
                    hideKeyboard(activity);
                }
            }
        });

        updateBookmarks();
        updateAutoComplete();

        omniboxRefresh.setOnClickListener(this);
        omniboxOverview.setOnClickListener(this);
    }

    private void performGesture (String gesture) {
        String fab_position = Objects.requireNonNull(sp.getString(gesture, "0"));
        ninjaWebView = (NinjaWebView) currentAlbumController;

        switch (fab_position) {
            case "01":

                break;
            case "02":
                if (ninjaWebView.canGoForward()) {
                    ninjaWebView.goForward();
                } else {
                    NinjaToast.show(context,R.string.toast_webview_forward);
                }
                break;
            case "03":
                if (ninjaWebView.canGoBack()) {
                    ninjaWebView.goBack();
                } else {
                    removeAlbum(currentAlbumController);
                }
                break;
            case "04":
                ninjaWebView.pageUp(true);
                break;
            case "05":
                ninjaWebView.pageDown(true);
                break;
            case "06":
                AlbumController controller = nextAlbumController(false);
                showAlbum(controller);
                break;
            case "07":
                AlbumController controller2 = nextAlbumController(true);
                showAlbum(controller2);
                break;
            case "08":
                showOverview();
                break;
            case "09":
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"), true);
                break;
            case "10":
                removeAlbum(currentAlbumController);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOverview() {

        bottomSheetDialog_OverView = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_overiew, null);

        open_startPage = dialogView.findViewById(R.id.open_newTab_2);
        open_bookmark = dialogView.findViewById(R.id.open_bookmark_2);
        open_history = dialogView.findViewById(R.id.open_history_2);
        open_menu = dialogView.findViewById(R.id.open_menu);
        tab_container = dialogView.findViewById(R.id.tab_container);
        tab_plus = dialogView.findViewById(R.id.tab_plus);
        tab_plus.setOnClickListener(this);
        tab_plus_bottom = dialogView.findViewById(R.id.tab_plus_bottom);
        tab_plus_bottom.setOnClickListener(this);
        tab_ScrollView = dialogView.findViewById(R.id.tab_ScrollView);
        overview_top = dialogView.findViewById(R.id.overview_top);
        overview_topButtons = dialogView.findViewById(R.id.overview_topButtons);
        listView = dialogView.findViewById(R.id.home_list_2);

        open_startPageView = dialogView.findViewById(R.id.open_newTabView);
        open_bookmarkView = dialogView.findViewById(R.id.open_bookmarkView);
        open_historyView = dialogView.findViewById(R.id.open_historyView);

        overview_titleIcons_startView = dialogView.findViewById(R.id.overview_titleIcons_startView);
        overview_titleIcons_bookmarksView = dialogView.findViewById(R.id.overview_titleIcons_bookmarksView);
        overview_titleIcons_historyView = dialogView.findViewById(R.id.overview_titleIcons_historyView);

        final GridView gridView = dialogView.findViewById(R.id.home_grid_2);

        final ImageButton overview_titleIcons_start = dialogView.findViewById(R.id.overview_titleIcons_start);
        final ImageButton overview_titleIcons_bookmarks = dialogView.findViewById(R.id.overview_titleIcons_bookmarks);
        final ImageButton overview_titleIcons_history = dialogView.findViewById(R.id.overview_titleIcons_history);

        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        // allow scrolling in listView without closing the bottomSheetDialog
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        if (listView.canScrollVertically(-1)) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        gridView.setOnTouchListener(new ListView.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        if (gridView.canScrollVertically(-1)) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_menu_overview, null);

                LinearLayout bookmark_sort = dialogView.findViewById(R.id.bookmark_sort);
                LinearLayout bookmark_filter = dialogView.findViewById(R.id.bookmark_filter);
                LinearLayout bookmark_blank = dialogView.findViewById(R.id.bookmark_blank);

                if (overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                    bookmark_filter.setVisibility(View.VISIBLE);
                    bookmark_sort.setVisibility(View.VISIBLE);
                } else if (overViewTab.equals(getString(R.string.album_title_home))){
                    bookmark_filter.setVisibility(View.GONE);
                    bookmark_sort.setVisibility(View.VISIBLE);
                } else if (overViewTab.equals(getString(R.string.album_title_history))){
                    bookmark_filter.setVisibility(View.GONE);
                    bookmark_sort.setVisibility(View.GONE);
                }

                bookmark_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_dialogFilter();
                    }
                });

                bookmark_blank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideBottomSheetDialog();
                        HelperUnit.setFavorite(context, "about:blank");
                    }
                });

                bookmark_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideBottomSheetDialog ();
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView = View.inflate(context, R.layout.dialog_bookmark_sort, null);
                        LinearLayout dialog_sortName = dialogView.findViewById(R.id.dialog_sortName);
                        TextView bookmark_sort_tv = dialogView.findViewById(R.id.bookmark_sort_tv);

                        if (overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                            bookmark_sort_tv.setText(getResources().getString(R.string.dialog_sortIcon));
                        } else if (overViewTab.equals(getString(R.string.album_title_home))){
                            bookmark_sort_tv.setText(getResources().getString(R.string.dialog_sortDate));
                        }

                        dialog_sortName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                                    sp.edit().putString("sortDBB", "title").apply();
                                    initBookmarkList();
                                    hideBottomSheetDialog ();
                                } else if (overViewTab.equals(getString(R.string.album_title_home))){
                                    sp.edit().putString("sort_startSite", "title").apply();
                                    open_startPage.performClick();
                                    hideBottomSheetDialog ();
                                }
                            }
                        });
                        LinearLayout dialog_sortIcon = dialogView.findViewById(R.id.dialog_sortIcon);
                        dialog_sortIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                                    sp.edit().putString("sortDBB", "icon").apply();
                                    initBookmarkList();
                                    hideBottomSheetDialog ();
                                } else if (overViewTab.equals(getString(R.string.album_title_home))){
                                    sp.edit().putString("sort_startSite", "ordinal").apply();
                                    open_startPage.performClick();
                                    hideBottomSheetDialog ();
                                }

                            }
                        });
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                    }
                });

                LinearLayout tv_delete = dialogView.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideBottomSheetDialog ();
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView3 = View.inflate(context, R.layout.dialog_action, null);
                        TextView textView = dialogView3.findViewById(R.id.dialog_text);
                        textView.setText(R.string.hint_database);
                        Button action_ok = dialogView3.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (overViewTab.equals(getString(R.string.album_title_home))) {
                                    BrowserUnit.clearHome(context);
                                    open_startPage.performClick();
                                } else if (overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                                    File data = Environment.getDataDirectory();
                                    String bookmarksPath_app = "//data//" + getPackageName() + "//databases//pass_DB_v01.db";
                                    final File bookmarkFile_app = new File(data, bookmarksPath_app);
                                    BrowserUnit.deleteDir(bookmarkFile_app);
                                    open_bookmark.performClick();
                                } else if (overViewTab.equals(getString(R.string.album_title_history))) {
                                    BrowserUnit.clearHistory(context);
                                    open_history.performClick();
                                }
                                hideBottomSheetDialog ();
                            }
                        });
                        Button action_cancel = dialogView3.findViewById(R.id.action_cancel);
                        action_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                            }
                        });
                        bottomSheetDialog.setContentView(dialogView3);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView3, BottomSheetBehavior.STATE_EXPANDED);
                    }
                });

                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bottomSheetDialog_OverView.setContentView(dialogView);

        mBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        int peekHeight = Math.round(200 * getResources().getDisplayMetrics().density);
        mBehavior.setPeekHeight(peekHeight);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    hideOverview();
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    if (sp.getBoolean("overView_hide", false)){
                        overview_top.setVisibility(View.GONE);
                    } else {
                        overview_topButtons.setVisibility(View.GONE);
                    }
                } else {
                    if (sp.getBoolean("overView_hide", false)){
                        overview_top.setVisibility(View.VISIBLE);
                    } else {
                        overview_topButtons.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        open_startPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                open_startPageView.setVisibility(View.VISIBLE);
                open_bookmarkView.setVisibility(View.INVISIBLE);
                open_historyView.setVisibility(View.INVISIBLE);
                overview_titleIcons_startView.setVisibility(View.VISIBLE);
                overview_titleIcons_bookmarksView.setVisibility(View.INVISIBLE);
                overview_titleIcons_historyView.setVisibility(View.INVISIBLE);
                overViewTab = getString(R.string.album_title_home);

                RecordAction action = new RecordAction(context);
                action.open(false);
                final List<GridItem> gridList = action.listGrid(context);
                action.close();

                gridAdapter = new xyz.johnzhang.frontier.View.GridAdapter(context, gridList);
                gridView.setAdapter(gridAdapter);
                gridAdapter.notifyDataSetChanged();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        updateAlbum(gridList.get(position).getURL());
                        hideOverview();
                    }
                });

                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        show_contextMenu_list(gridList.get(position).getTitle(), gridList.get(position).getURL(),
                                null, null, 0,
                                null, null, null , null, gridList.get(position));
                        return true;
                    }
                });
            }
        });

        open_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                open_startPageView.setVisibility(View.INVISIBLE);
                open_bookmarkView.setVisibility(View.VISIBLE);
                open_historyView.setVisibility(View.INVISIBLE);
                overview_titleIcons_startView.setVisibility(View.INVISIBLE);
                overview_titleIcons_bookmarksView.setVisibility(View.VISIBLE);
                overview_titleIcons_historyView.setVisibility(View.INVISIBLE);
                overViewTab = getString(R.string.album_title_bookmarks);
                sp.edit().putString("filter_passBY", "00").apply();
                initBookmarkList();
            }
        });

        open_bookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFilter();
                return false;
            }
        });

        open_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                open_startPageView.setVisibility(View.INVISIBLE);
                open_bookmarkView.setVisibility(View.INVISIBLE);
                open_historyView.setVisibility(View.VISIBLE);
                overview_titleIcons_startView.setVisibility(View.INVISIBLE);
                overview_titleIcons_bookmarksView.setVisibility(View.INVISIBLE);
                overview_titleIcons_historyView.setVisibility(View.VISIBLE);
                overViewTab = getString(R.string.album_title_history);

                RecordAction action = new RecordAction(context);
                action.open(false);
                final List<Record> list;
                list = action.listHistory();
                action.close();

                final Adapter_Record adapter = new Adapter_Record(context, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        updateAlbum(list.get(position).getURL());
                        hideOverview();
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        show_contextMenu_list(list.get(position).getTitle(), list.get(position).getURL(), adapter, list, position,
                                null, null, null , null, null);
                        return true;
                    }
                });
            }
        });

        overview_titleIcons_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_startPage.performClick();
            }
        });

        overview_titleIcons_bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_bookmark.performClick();
            }
        });

        overview_titleIcons_bookmarks.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!overViewTab.equals(getString(R.string.album_title_bookmarks))) {
                open_bookmark.performClick();
                }
                show_dialogFilter();
                return false;
            }
        });

        overview_titleIcons_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_history.performClick();
            }
        });

        switch (Objects.requireNonNull(sp.getString("start_tab", "0"))) {

            case "0":
                overview_top.setVisibility(View.GONE);
                open_startPage.performClick();
                break;
            case "3":
                overview_top.setVisibility(View.GONE);
                open_bookmark.performClick();
                break;
            case "4":
                overview_top.setVisibility(View.GONE);
                open_history.performClick();
                break;
            default:
                overview_top.setVisibility(View.GONE);
                open_startPage.performClick();
                break;
        }
    }

    private void initSearchPanel() {
        searchPanel = findViewById(R.id.main_search_panel);
        searchBox = findViewById(R.id.main_search_box);
        searchUp = findViewById(R.id.main_search_up);
        searchDown = findViewById(R.id.main_search_down);
        searchCancel = findViewById(R.id.main_search_cancel);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (currentAlbumController != null) {
                    ((NinjaWebView) currentAlbumController).findAllAsync(s.toString());
                }
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }
                if (searchBox.getText().toString().isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return true;
                }
                return false;
            }
        });

        searchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return;
                }
                hideKeyboard(activity);
                ((NinjaWebView) currentAlbumController).findNext(false);
            }
        });

        searchDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return;
                }
                hideKeyboard(activity);
                ((NinjaWebView) currentAlbumController).findNext(true);
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchPanel();
            }
        });
    }



    private void initBookmarkList() {

        final BookmarkList db = new BookmarkList(context);
        final Cursor row;
        db.open();

        final int layoutStyle = R.layout.list_item_bookmark;
        int[] xml_id = new int[] {
                R.id.record_item_title
        };
        String[] column = new String[] {
                "pass_title",
        };

        String search = sp.getString("filter_passBY", "00");

        if (Objects.requireNonNull(search).equals("00")) {
            row = db.fetchAllData(activity);
        } else {
            row = db.fetchDataByFilter(search, "pass_creation");
        }

        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, layoutStyle, row, column, xml_id, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                Cursor row = (Cursor) listView.getItemAtPosition(position);
                final String bookmarks_icon = row.getString(row.getColumnIndexOrThrow("pass_creation"));

                View v = super.getView(position, convertView, parent);
                ImageView iv_icon = v.findViewById(R.id.ib_icon);
                HelperUnit.switchIcon(activity, bookmarks_icon, "pass_creation", iv_icon);

                return v;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String pass_content = row.getString(row.getColumnIndexOrThrow("pass_content"));
                final String pass_icon = row.getString(row.getColumnIndexOrThrow("pass_icon"));
                final String pass_attachment = row.getString(row.getColumnIndexOrThrow("pass_attachment"));
                updateAlbum(pass_content);
                toast_login (pass_icon, pass_attachment);
                hideOverview();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor row = (Cursor) listView.getItemAtPosition(position);
                final String _id = row.getString(row.getColumnIndexOrThrow("_id"));
                final String pass_title = row.getString(row.getColumnIndexOrThrow("pass_title"));
                final String pass_content = row.getString(row.getColumnIndexOrThrow("pass_content"));
                final String pass_icon = row.getString(row.getColumnIndexOrThrow("pass_icon"));
                final String pass_attachment = row.getString(row.getColumnIndexOrThrow("pass_attachment"));
                final String pass_creation = row.getString(row.getColumnIndexOrThrow("pass_creation"));

                show_contextMenu_list(pass_title, pass_content, null, null, 0,
                        pass_icon, pass_attachment, _id , pass_creation, null);
                return true;
            }
        });
    }

    private void show_dialogFastToggle() {

        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_toggle, null);

        CheckBox sw_java = dialogView.findViewById(R.id.switch_js);
        final ImageButton whiteList_js = dialogView.findViewById(R.id.imageButton_js);
        CheckBox sw_adBlock = dialogView.findViewById(R.id.switch_adBlock);
        final ImageButton whiteList_ab = dialogView.findViewById(R.id.imageButton_ab);
        CheckBox sw_cookie = dialogView.findViewById(R.id.switch_cookie);
        final ImageButton whitelist_cookie = dialogView.findViewById(R.id.imageButton_cookie);

        javaHosts = new Javascript(context);
        javaHosts = getJavaHosts();
        cookieHosts = new Cookie(context);
        cookieHosts = getCookieHosts();
        adBlock = new AdBlock(context);
        adBlock = getAdBlock();
        ninjaWebView = (NinjaWebView) currentAlbumController;

        final String url = ninjaWebView.getUrl();

        if (sp.getBoolean(getString(R.string.sp_javascript), true)){
            sw_java.setChecked(true);
        } else {
            sw_java.setChecked(false);
        }

        if (sp.getBoolean(getString(R.string.sp_ad_block), true)){
            sw_adBlock.setChecked(true);
        } else {
            sw_adBlock.setChecked(false);
        }

        if (sp.getBoolean(getString(R.string.sp_cookies), true)){
            sw_cookie.setChecked(true);
        } else {
            sw_cookie.setChecked(false);
        }

        if (javaHosts.isWhite(url)) {
            whiteList_js.setImageResource(R.drawable.check_green);
        } else {
            whiteList_js.setImageResource(R.drawable.ic_action_close_red);
        }

        if (cookieHosts.isWhite(url)) {
            whitelist_cookie.setImageResource(R.drawable.check_green);
        } else {
            whitelist_cookie.setImageResource(R.drawable.ic_action_close_red);
        }

        if (adBlock.isWhite(url)) {
            whiteList_ab.setImageResource(R.drawable.check_green);
        } else {
            whiteList_ab.setImageResource(R.drawable.ic_action_close_red);
        }

        whiteList_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (javaHosts.isWhite(ninjaWebView.getUrl())) {
                    whiteList_js.setImageResource(R.drawable.ic_action_close_red);
                    javaHosts.removeDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                } else {
                    whiteList_js.setImageResource(R.drawable.check_green);
                    javaHosts.addDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                }
            }
        });

        whitelist_cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cookieHosts.isWhite(ninjaWebView.getUrl())) {
                    whitelist_cookie.setImageResource(R.drawable.ic_action_close_red);
                    cookieHosts.removeDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                } else {
                    whitelist_cookie.setImageResource(R.drawable.check_green);
                    cookieHosts.addDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                }
            }
        });


        whiteList_ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adBlock.isWhite(ninjaWebView.getUrl())) {
                    whiteList_ab.setImageResource(R.drawable.ic_action_close_red);
                    adBlock.removeDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                } else {
                    whiteList_ab.setImageResource(R.drawable.check_green);
                    adBlock.addDomain(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim());
                }
            }
        });

        sw_java.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean(getString(R.string.sp_javascript), true).commit();
                }else{
                    sp.edit().putBoolean(getString(R.string.sp_javascript), false).commit();
                }

            }
        });

        sw_adBlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean(getString(R.string.sp_ad_block), true).commit();
                }else{
                    sp.edit().putBoolean(getString(R.string.sp_ad_block), false).commit();
                }
            }
        });

        sw_cookie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    sp.edit().putBoolean(getString(R.string.sp_cookies), true).commit();
                }else{
                    sp.edit().putBoolean(getString(R.string.sp_cookies), false).commit();
                }
            }
        });

        final ImageButton toggle_history = dialogView.findViewById(R.id.toggle_history);
        final View toggle_historyView = dialogView.findViewById(R.id.toggle_historyView);

        final ImageButton toggle_location = dialogView.findViewById(R.id.toggle_location);
        final View toggle_locationView = dialogView.findViewById(R.id.toggle_locationView);

        final ImageButton toggle_images = dialogView.findViewById(R.id.toggle_images);
        final View toggle_imagesView = dialogView.findViewById(R.id.toggle_imagesView);

        final ImageButton toggle_remote = dialogView.findViewById(R.id.toggle_remote);
        final View toggle_remoteView = dialogView.findViewById(R.id.toggle_remoteView);

        final ImageButton toggle_invert = dialogView.findViewById(R.id.toggle_invert);
        final View toggle_invertView = dialogView.findViewById(R.id.toggle_invertView);

        final ImageButton toggle_font = dialogView.findViewById(R.id.toggle_font);

        toggle_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                Intent intent = new Intent(context, Settings_Activity.class);
                startActivity(intent);
            }
        });

        if (sp.getBoolean("saveHistory", false)) {
            toggle_historyView.setVisibility(View.VISIBLE);
        } else {
            toggle_historyView.setVisibility(View.INVISIBLE);
        }

        toggle_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("saveHistory", false)) {
                    toggle_historyView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("saveHistory", false).commit();
                } else {
                    toggle_historyView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("saveHistory", true).commit();
                }
            }
        });

        if (sp.getBoolean(getString(R.string.sp_location), false)) {
            toggle_locationView.setVisibility(View.VISIBLE);
        } else {
            toggle_locationView.setVisibility(View.INVISIBLE);
        }

        toggle_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean(getString(R.string.sp_location), false)) {
                    toggle_locationView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_location), false).commit();
                } else {
                    toggle_locationView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_location), true).commit();
                }
            }
        });

        if (sp.getBoolean(getString(R.string.sp_images), true)) {
            toggle_imagesView.setVisibility(View.VISIBLE);
        } else {
            toggle_imagesView.setVisibility(View.INVISIBLE);
        }

        toggle_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean(getString(R.string.sp_images), true)) {
                    toggle_imagesView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_images), false).commit();
                } else {
                    toggle_imagesView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_images), true).commit();
                }
            }
        });

        if (sp.getBoolean("sp_remote", true)) {
            toggle_remoteView.setVisibility(View.VISIBLE);
        } else {
            toggle_remoteView.setVisibility(View.INVISIBLE);
        }

        toggle_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("sp_remote", true)) {
                    toggle_remoteView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("sp_remote", false).commit();
                } else {
                    toggle_remoteView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("sp_remote", true).commit();
                }
            }
        });

        if (sp.getBoolean("sp_invert", false)) {
            toggle_invertView.setVisibility(View.VISIBLE);
        } else {
            toggle_invertView.setVisibility(View.INVISIBLE);
        }

        toggle_invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("sp_invert", false)) {
                    toggle_invertView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("sp_invert", false).commit();
                } else {
                    toggle_invertView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("sp_invert", true).commit();
                }
                initRendering(contentFrame);
            }
        });

        Button but_OK = dialogView.findViewById(R.id.action_ok);
        but_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ninjaWebView != null) {
                    hideBottomSheetDialog ();
                    ninjaWebView.initPreferences();
                    ninjaWebView.reload();
                }
            }
        });

        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBottomSheetDialog ();
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void toast_login (String userName, String passWord) {
        try {
            final String decrypted_userName = mahEncryptor.decode(userName);
            final String decrypted_userPW = mahEncryptor.decode(passWord);
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;

            final BroadcastReceiver unCopy = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ClipData clip = ClipData.newPlainText("text", decrypted_userName);
                    clipboard.setPrimaryClip(clip);
                    NinjaToast.show(context, R.string.toast_copy_successful);
                }
            };

            final BroadcastReceiver pwCopy = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ClipData clip = ClipData.newPlainText("text", decrypted_userPW);
                    clipboard.setPrimaryClip(clip);
                    NinjaToast.show(context, R.string.toast_copy_successful);
                }
            };

            IntentFilter intentFilter = new IntentFilter("unCopy");
            registerReceiver(unCopy, intentFilter);
            Intent copy = new Intent("unCopy");
            PendingIntent copyUN = PendingIntent.getBroadcast(context, 0, copy, PendingIntent.FLAG_CANCEL_CURRENT);

            IntentFilter intentFilter2 = new IntentFilter("pwCopy");
            registerReceiver(pwCopy, intentFilter2);
            Intent copy2 = new Intent("pwCopy");
            PendingIntent copyPW = PendingIntent.getBroadcast(context, 1, copy2, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder;

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "browser_not";// The id of the channel.
                CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(mChannel);
                builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            } else {
                //noinspection deprecation
                builder = new NotificationCompat.Builder(context);
            }

            NotificationCompat.Action action_UN = new NotificationCompat.Action.Builder(R.drawable.icon_earth, getString(R.string.toast_titleConfirm_pasteUN), copyUN).build();
            NotificationCompat.Action action_PW = new NotificationCompat.Action.Builder(R.drawable.icon_earth, getString(R.string.toast_titleConfirm_pastePW), copyPW).build();

            @SuppressWarnings("deprecation")
            Notification n  = builder
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_notification_ninja)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.toast_titleConfirm_paste))
                    .setColor(ContextCompat.getColor(context,R.color.colorAccent))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .addAction(action_UN)
                    .addAction(action_PW)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;

            if (decrypted_userName.length() > 0 || decrypted_userPW.length() > 0 ) {
                notificationManager.notify(0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            NinjaToast.show(context, R.string.toast_error);
        }
    }


    private synchronized void addAlbum(String title, final String url, final boolean foreground) {

        showOmnibox();
        ninjaWebView = new NinjaWebView(context);
        ninjaWebView.setBrowserController(this);
        ninjaWebView.setAlbumTitle(title);
        ViewUnit.bound(context, ninjaWebView);

        final View albumView = ninjaWebView.getAlbumView();
        if (currentAlbumController != null) {
            int index = BrowserContainer.indexOf(currentAlbumController) + 1;
            BrowserContainer.add(ninjaWebView, index);
            tab_container.addView(albumView, index, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            BrowserContainer.add(ninjaWebView);
            tab_container.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        if (!foreground) {
            ViewUnit.bound(context, ninjaWebView);
            ninjaWebView.loadUrl(url);
            ninjaWebView.deactivate();
            return;
        }

        showAlbum(ninjaWebView);

        if (url != null && !url.isEmpty()) {
            ninjaWebView.loadUrl(url);
        }
    }

    private synchronized void pinAlbums(String url) {

        showOmnibox();
        hideSearchPanel();
        tab_container.removeAllViews();

        ninjaWebView = new NinjaWebView(context);

        for (AlbumController controller : BrowserContainer.list()) {
            ((NinjaWebView) controller).setBrowserController(this);
            tab_container.addView(controller.getAlbumView(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            controller.getAlbumView().setVisibility(View.VISIBLE);
            controller.deactivate();
        }

        if (BrowserContainer.size() < 1 && url == null) {
            addAlbum("", sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"), true);
            if (android.os.Build.VERSION.SDK_INT < 28) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        omniboxRefresh.performClick();
                    }
                }, 500);
            }
        } else if (BrowserContainer.size() >= 1 && url == null) {

            int index = BrowserContainer.size() - 1;
            currentAlbumController = BrowserContainer.get(index);
            contentFrame.removeAllViews();
            contentFrame.addView((View) currentAlbumController);
            currentAlbumController.activate();

        } else if (url != null) { // When url != null
            ninjaWebView.setBrowserController(this);
            ninjaWebView.setAlbumTitle(getString(R.string.app_name));
            ViewUnit.bound(context, ninjaWebView);
            ninjaWebView.loadUrl(url);

            BrowserContainer.add(ninjaWebView);
            final View albumView = ninjaWebView.getAlbumView();
            tab_container.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            contentFrame.removeAllViews();
            contentFrame.addView(ninjaWebView);

            currentAlbumController = ninjaWebView;
            currentAlbumController.activate();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlbum(currentAlbumController);
            }
        }, shortAnimTime);
    }

    private synchronized void updateAlbum(String url) {
        ((NinjaWebView) currentAlbumController).loadUrl(url);
        updateOmnibox();
    }

    private void closeTabConfirmation(final Runnable okAction) {
        if(!sp.getBoolean("sp_close_tab_confirm", false)) {
            okAction.run();
        } else {
            bottomSheetDialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);
            textView.setText(R.string.toast_close_tab);
            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okAction.run();
                    hideBottomSheetDialog ();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideBottomSheetDialog ();
                }
            });
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public synchronized void removeAlbum(final AlbumController controller) {

        if (BrowserContainer.size() <= 1) {
            if(!sp.getBoolean("sp_reopenLastTab", false)) {
                doubleTapsQuit();
            }else{
                updateAlbum(sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"));
                hideOverview();
            }
        } else {
            closeTabConfirmation( new Runnable() {
                @Override
                public void run() {
                    tab_container.removeView(controller.getAlbumView());
                    int index = BrowserContainer.indexOf(controller);
                    BrowserContainer.remove(controller);
                    if (index >= BrowserContainer.size()) {
                        index = BrowserContainer.size() - 1;
                    }
                    showAlbum(BrowserContainer.get(index));
                }
            });
        }
    }

    private void updateOmnibox() {
        if (ninjaWebView == currentAlbumController) {
            omniboxTitle.setText(currentAlbumController.getAlbumTitle());
            setColor();
        } else {
            ninjaWebView = (NinjaWebView) currentAlbumController;
            updateProgress(ninjaWebView.getProgress());
        }
    }

    private void scrollChange () {

        if (Objects.requireNonNull(sp.getString("sp_hideToolbar", "0")).equals("0") ||
                Objects.requireNonNull(sp.getString("sp_hideToolbar", "0")).equals("1")) {

            ninjaWebView.setOnScrollChangeListener(new NinjaWebView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(int scrollY, int oldScrollY) {
                    int height = (int) Math.floor(ninjaWebView.getContentHeight() * ninjaWebView.getResources().getDisplayMetrics().density);
                    int webViewHeight = ninjaWebView.getHeight();
                    int cutoff = height - webViewHeight - 112 * Math.round(getResources().getDisplayMetrics().density); // Don't be too strict on the cutoff point

                    if (Objects.requireNonNull(sp.getString("sp_hideToolbar", "0")).equals("0")) {
                        if (scrollY > oldScrollY && cutoff >= scrollY) {
                            hideOmnibox();
                        } else if (scrollY < oldScrollY){
                            showOmnibox();
                        }
                    } else if (Objects.requireNonNull(sp.getString("sp_hideToolbar", "0")).equals("1")) {
                        hideOmnibox();
                    }
                }
            });
        }
    }

    private static boolean isDarkColor(int color) {
        if (android.R.color.transparent == color)
            return false;

        boolean rtnValue = false;
        int[] rgb = { Color.red(color), Color.green(color), Color.blue(color) };
        int brightness = (int) Math.sqrt(rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068);
        // color is light
        if (brightness >= 200) {
            rtnValue = true;
        }
        return !rtnValue;
    }

    @Override
    public synchronized void updateProgress(int progress) {
        progressBar.setProgress(progress);

        updateOmnibox();
        setColor();
        scrollChange();
        initRendering(contentFrame);
        ninjaWebView.requestFocus();

        if (progress < BrowserUnit.PROGRESS_MAX) {
            updateRefresh(true);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            updateBookmarks();
            updateRefresh(false);
            progressBar.setVisibility(View.GONE);
            currentAlbumController.setAlbumCover(ViewUnit.capture(((View) currentAlbumController), dimen144dp, dimen108dp, Bitmap.Config.RGB_565));
        }
    }

    private void updateRefresh(boolean running) {
        if (running) {
            omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(context, R.drawable.ic_action_close));
        } else {
            try {
                if (ninjaWebView.getUrl().contains("https://")) {
                    omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(context, R.drawable.ic_action_refresh));
                } else {
                    omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(context, R.drawable.icon_alert));
                }
            } catch (Exception e) {
                omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(context, R.drawable.ic_action_refresh));
            }
        }
    }


    @Override
    public void showFileChooser(ValueCallback<Uri[]> filePathCallback) {

        if(mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (Exception ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");

        Intent[] intentArray;
        if(takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    private File createImageFile() {
        String time = Objects.toString(System.currentTimeMillis(), null);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File tempDir = new File(storageDir + "/FOSS_Browser_temp");
        tempDir.mkdirs();
        String extension = time + ".jpg";
        return new File(tempDir, extension);
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (view == null) {
            return;
        }
        if (customView != null && callback != null) {
            callback.onCustomViewHidden();
            return;
        }

        customView = view;
        originalOrientation = getRequestedOrientation();

        fullscreenHolder = new FullscreenHolder(context);
        fullscreenHolder.addView(
                customView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(
                fullscreenHolder,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        customView.setKeepScreenOn(true);
        ((View) currentAlbumController).setVisibility(View.GONE);
        setCustomFullscreen(true);

        if (view instanceof FrameLayout) {
            if (((FrameLayout) view).getFocusedChild() instanceof VideoView) {
                videoView = (VideoView) ((FrameLayout) view).getFocusedChild();
                videoView.setOnErrorListener(new VideoCompletionListener());
                videoView.setOnCompletionListener(new VideoCompletionListener());
            }
        }
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean onHideCustomView() {
        if (customView == null || customViewCallback == null || currentAlbumController == null) {
            return false;
        }

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.removeView(fullscreenHolder);

        customView.setKeepScreenOn(false);
        ((View) currentAlbumController).setVisibility(View.VISIBLE);
        setCustomFullscreen(false);

        fullscreenHolder = null;
        customView = null;
        if (videoView != null) {
            videoView.setOnErrorListener(null);
            videoView.setOnCompletionListener(null);
            videoView = null;
        }
        setRequestedOrientation(originalOrientation);
        ninjaWebView.reload();

        return true;
    }

    private void show_contextMenu_link(final String url) {
        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_menu_context_link, null);
        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(url);

        LinearLayout contextLink_newTab = dialogView.findViewById(R.id.contextLink_newTab);
        contextLink_newTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, false);
                NinjaToast.show(context, getString(R.string.toast_new_tab_successful));
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink__shareLink = dialogView.findViewById(R.id.contextLink__shareLink);
        contextLink__shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prepareRecord()) {
                    NinjaToast.show(context, getString(R.string.toast_share_failed));
                } else {
                    IntentUnit.share(context, "", url);
                }
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink_openWith = dialogView.findViewById(R.id.contextLink_openWith);
        contextLink_openWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                Intent chooser = Intent.createChooser(intent, getString(R.string.menu_open_with));
                startActivity(chooser);
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink_newTabOpen = dialogView.findViewById(R.id.contextLink_newTabOpen);
        contextLink_newTabOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, true);
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextMenu_saveStart = dialogView.findViewById(R.id.contextMenu_saveStart);
        contextMenu_saveStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                RecordAction action = new RecordAction(context);
                action.open(true);
                if (action.checkGridItem(url)) {
                    NinjaToast.show(context, getString(R.string.toast_already_exist_in_home));
                } else {

                    int counter = sp.getInt("counter", 0);
                    counter = counter + 1;
                    sp.edit().putInt("counter", counter).commit();

                    Bitmap bitmap = ViewUnit.createImage(3, 3, vibrantColor);
                    String filename = counter + BrowserUnit.SUFFIX_PNG;

                    GridItem itemAlbum = new GridItem(Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim(),
                            url, filename, counter);

                    if (BrowserUnit.bitmap2File(context, bitmap, filename) && action.addGridItem(itemAlbum)) {
                        NinjaToast.show(context, getString(R.string.toast_add_to_home_successful));
                    } else {
                        NinjaToast.show(context, getString(R.string.toast_add_to_home_failed));
                    }
                }
                action.close();
            }
        });

        LinearLayout contextLink_sc = dialogView.findViewById(R.id.contextLink_sc);
        contextLink_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.createShortcut(context, Objects.requireNonNull(Uri.parse(url).getHost()).replace("www.", "").trim(), url);
            }
        });

        LinearLayout contextLink_saveAs = dialogView.findViewById(R.id.contextLink_saveAs);
        contextLink_saveAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    hideBottomSheetDialog ();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View dialogView = View.inflate(context, R.layout.dialog_edit_extension, null);

                    final EditText editTitle = dialogView.findViewById(R.id.dialog_edit);
                    final EditText editExtension = dialogView.findViewById(R.id.dialog_edit_extension);

                    String filename = URLUtil.guessFileName(url, null, null);

                    editTitle.setHint(R.string.dialog_title_hint);
                    editTitle.setText(HelperUnit.fileName(ninjaWebView.getUrl()));

                    String extension = filename.substring(filename.lastIndexOf("."));
                    if(extension.length() <= 8) {
                        editExtension.setText(extension);
                    }

                    builder.setView(dialogView);
                    builder.setTitle(R.string.menu_edit);
                    builder.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String title = editTitle.getText().toString().trim();
                            String extension = editExtension.getText().toString().trim();
                            String  filename = title + extension;

                            if (title.isEmpty() || extension.isEmpty() || !extension.startsWith(".")) {
                                NinjaToast.show(context, getString(R.string.toast_input_empty));
                            } else {

                                if (android.os.Build.VERSION.SDK_INT >= 23) {
                                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                                        HelperUnit.grantPermissionsStorage(activity);
                                    } else {
                                        Uri source = Uri.parse(url);
                                        DownloadManager.Request request = new DownloadManager.Request(source);
                                        request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                        assert dm != null;
                                        dm.enqueue(request);
                                        hideKeyboard(activity);
                                    }
                                } else {
                                    Uri source = Uri.parse(url);
                                    DownloadManager.Request request = new DownloadManager.Request(source);
                                    request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    assert dm != null;
                                    dm.enqueue(request);
                                    hideKeyboard(activity);
                                }
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                            hideKeyboard(activity);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onLongPress(final String url) {
        WebView.HitTestResult result = ninjaWebView.getHitTestResult();
        if (url != null) {
            show_contextMenu_link(url);
        } else if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
            show_contextMenu_link(result.getExtra());
        }
    }

    private void doubleTapsQuit() {
        if (!sp.getBoolean("sp_close_browser_confirm", true)) {
            finish();
        } else {
            bottomSheetDialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);
            textView.setText(R.string.toast_quit);
            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideBottomSheetDialog ();
                }
            });
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("RestrictedApi")
    private void showOmnibox() {
        if (omnibox.getVisibility() == View.GONE && searchPanel.getVisibility() == View.GONE) {

            String showNavButton = Objects.requireNonNull(sp.getString("sp_hideNav", "0"));

            switch (showNavButton) {
                case "0":
                    fab_imageButtonNav.setVisibility(View.GONE);
                    break;
                case "1":
                    fab_imageButtonNav.setVisibility(View.GONE);
                    break;
                default:
                    fab_imageButtonNav.setVisibility(View.VISIBLE);
                    break;
            }

            if (omnibox.getVisibility() == View.GONE) {
                searchPanel.setVisibility(View.GONE);
                omnibox.setVisibility(View.VISIBLE);
                omniboxTitle.setVisibility(View.VISIBLE);
                appBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void hideOmnibox() {

        String showNavButton = Objects.requireNonNull(sp.getString("sp_hideNav", "0"));

        switch (showNavButton) {
            case "0":
                fab_imageButtonNav.setVisibility(View.VISIBLE);
                break;
            case "1":
                fab_imageButtonNav.setVisibility(View.GONE);
                break;
            default:
                fab_imageButtonNav.setVisibility(View.VISIBLE);
                break;
        }

        if (omnibox.getVisibility() == View.VISIBLE) {
            omnibox.setVisibility(View.GONE);
            searchPanel.setVisibility(View.GONE);
            appBar.setVisibility(View.GONE);
        }
    }

    private void hideSearchPanel() {
        hideKeyboard(activity);
        omniboxTitle.setVisibility(View.VISIBLE);
        searchBox.setText("");
        searchPanel.setVisibility(View.GONE);
        omnibox.setVisibility(View.VISIBLE);
    }

    private void showSearchPanel() {
        showOmnibox();
        omnibox.setVisibility(View.GONE);
        omniboxTitle.setVisibility(View.GONE);
        searchPanel.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("SameReturnValue")
    private boolean showOverflow() {

        bottomSheetDialog = new BottomSheetDialog(context);

        View dialogView = View.inflate(context, R.layout.dialog_menu, null);

        fab_tab = dialogView.findViewById(R.id.floatButton_tab);
        fab_tab.setOnClickListener(BrowserActivity.this);
        fab_share = dialogView.findViewById(R.id.floatButton_share);
        fab_share.setOnClickListener(BrowserActivity.this);
        fab_save = dialogView.findViewById(R.id.floatButton_save);
        fab_save.setOnClickListener(BrowserActivity.this);
        fab_more = dialogView.findViewById(R.id.floatButton_more);
        fab_more.setOnClickListener(BrowserActivity.this);

        floatButton_tabView = dialogView.findViewById(R.id.floatButton_tabView);
        floatButton_saveView = dialogView.findViewById(R.id.floatButton_saveView);
        floatButton_shareView = dialogView.findViewById(R.id.floatButton_shareView);
        floatButton_moreView = dialogView.findViewById(R.id.floatButton_moreView);

        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(ninjaWebView.getTitle());

        menu_newTabOpen = dialogView.findViewById(R.id.menu_newTabOpen);
        menu_newTabOpen.setOnClickListener(BrowserActivity.this);
        menu_closeTab = dialogView.findViewById(R.id.menu_closeTab);
        menu_closeTab.setOnClickListener(BrowserActivity.this);
        menu_tabPreview = dialogView.findViewById(R.id.menu_tabPreview);
        menu_tabPreview.setOnClickListener(BrowserActivity.this);
        menu_quit = dialogView.findViewById(R.id.menu_quit);
        menu_quit.setOnClickListener(BrowserActivity.this);

        menu_shareScreenshot = dialogView.findViewById(R.id.menu_shareScreenshot);
        menu_shareScreenshot.setOnClickListener(BrowserActivity.this);
        menu_shareLink = dialogView.findViewById(R.id.menu_shareLink);
        menu_shareLink.setOnClickListener(BrowserActivity.this);
        menu_sharePDF = dialogView.findViewById(R.id.menu_sharePDF);
        menu_sharePDF.setOnClickListener(BrowserActivity.this);
        menu_openWith = dialogView.findViewById(R.id.menu_openWith);
        menu_openWith.setOnClickListener(BrowserActivity.this);

        menu_saveScreenshot = dialogView.findViewById(R.id.menu_saveScreenshot);
        menu_saveScreenshot.setOnClickListener(BrowserActivity.this);
        menu_saveBookmark = dialogView.findViewById(R.id.menu_saveBookmark);
        menu_saveBookmark.setOnClickListener(BrowserActivity.this);
        menu_savePDF = dialogView.findViewById(R.id.contextLink_saveAs);
        menu_savePDF.setOnClickListener(BrowserActivity.this);
        menu_saveStart = dialogView.findViewById(R.id.menu_saveStart);
        menu_saveStart.setOnClickListener(BrowserActivity.this);

        menu_searchSite = dialogView.findViewById(R.id.menu_searchSite);
        menu_searchSite.setOnClickListener(BrowserActivity.this);
        menu_settings = dialogView.findViewById(R.id.menu_settings);
        menu_settings.setOnClickListener(BrowserActivity.this);
        menu_download = dialogView.findViewById(R.id.menu_download);
        menu_download.setOnClickListener(BrowserActivity.this);
        menu_help = dialogView.findViewById(R.id.menu_help);
        menu_help.setOnClickListener(BrowserActivity.this);

        menu_shareCLipboard = dialogView.findViewById(R.id.menu_shareCLipboard);
        menu_shareCLipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", url);
                clipboard.setPrimaryClip(clip);
                NinjaToast.show(context, R.string.toast_copy_successful);
            }
        });
        menu_openFav = dialogView.findViewById(R.id.menu_openFav);
        menu_openFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                updateAlbum(sp.getString("favoriteURL", "https://github.com/ZsgsDesign/frontier_android"));
            }
        });
        menu_sc = dialogView.findViewById(R.id.menu_sc);
        menu_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.createShortcut(context, ninjaWebView.getTitle(), ninjaWebView.getUrl());
            }
        });
        menu_fav = dialogView.findViewById(R.id.menu_fav);
        menu_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.setFavorite(context, url);
            }
        });

        floatButton_tabView.setBackgroundColor(vibrantColor);
        floatButton_saveView.setBackgroundColor(vibrantColor);
        floatButton_shareView.setBackgroundColor(vibrantColor);
        floatButton_moreView.setBackgroundColor(vibrantColor);

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        return true;
    }

    private void show_contextMenu_list (final String title, final String url,
                                       final Adapter_Record adapterRecord, final List<Record> recordList, final int location,
                                       final String userName, final String userPW, final String _id, final String pass_creation,
                                       final GridItem gridItem) {


        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_menu_context_list, null);

        final BookmarkList db = new BookmarkList(context);
        db.open();

        LinearLayout contextList_edit = dialogView.findViewById(R.id.menu_contextList_edit);
        LinearLayout contextList_fav = dialogView.findViewById(R.id.menu_contextList_fav);
        LinearLayout contextList_sc = dialogView.findViewById(R.id.menu_contextLink_sc);
        LinearLayout contextList_newTab = dialogView.findViewById(R.id.menu_contextList_newTab);
        LinearLayout contextList_newTabOpen = dialogView.findViewById(R.id.menu_contextList_newTabOpen);
        LinearLayout contextList_delete = dialogView.findViewById(R.id.menu_contextList_delete);

        if (overViewTab.equals(getString(R.string.album_title_history))) {
            contextList_edit.setVisibility(View.GONE);
        } else {
            contextList_edit.setVisibility(View.VISIBLE);
        }

        contextList_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.setFavorite(context, url);
            }
        });

        contextList_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.createShortcut(context, title, url);
            }
        });

        contextList_newTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, false);
                NinjaToast.show(context, getString(R.string.toast_new_tab_successful));
                hideBottomSheetDialog ();
            }
        });

        contextList_newTabOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, true);
                hideBottomSheetDialog ();
                hideOverview();
            }
        });

        contextList_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_titleConfirm_delete);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (overViewTab.equals(getString(R.string.album_title_home))) {
                            RecordAction action = new RecordAction(context);
                            action.open(true);
                            action.deleteGridItem(gridItem);
                            action.close();
                            deleteFile(gridItem.getFilename());
                            open_startPage.performClick();
                            hideBottomSheetDialog ();
                        } else if (overViewTab.equals(getString(R.string.album_title_bookmarks))){
                            db.delete(Integer.parseInt(_id));
                            initBookmarkList();
                            hideBottomSheetDialog ();
                        } else if (overViewTab.equals(getString(R.string.album_title_history))){
                            Record record = recordList.get(location);
                            RecordAction action = new RecordAction(context);
                            action.open(true);
                            action.deleteHistory(record);
                            action.close();
                            recordList.remove(location);
                            adapterRecord.notifyDataSetChanged();
                            updateBookmarks();
                            updateAutoComplete();
                            hideBottomSheetDialog ();
                        }

                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideBottomSheetDialog ();
                    }
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        contextList_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();

                if (overViewTab.equals(getString(R.string.album_title_home))) {
                    bottomSheetDialog = new BottomSheetDialog(context);
                    View dialogView = View.inflate(context, R.layout.dialog_edit_title, null);

                    final EditText editText = dialogView.findViewById(R.id.dialog_edit);

                    editText.setHint(R.string.dialog_title_hint);
                    editText.setText(title);

                    Button action_ok = dialogView.findViewById(R.id.action_ok);
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = editText.getText().toString().trim();
                            if (text.isEmpty()) {
                                NinjaToast.show(context, getString(R.string.toast_input_empty));
                            } else {
                                RecordAction action = new RecordAction(context);
                                action.open(true);
                                gridItem.setTitle(text);
                                action.updateGridItem(gridItem);
                                action.close();
                                hideKeyboard(activity);
                                open_startPage.performClick();
                            }
                            hideBottomSheetDialog ();
                        }
                    });
                    Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                    action_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard(activity);
                            hideBottomSheetDialog ();
                        }
                    });
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                    HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                } else if (overViewTab.equals(getString(R.string.album_title_bookmarks))){
                    try {

                        bottomSheetDialog = new BottomSheetDialog(context);

                        View dialogView = View.inflate(context, R.layout.dialog_edit_bookmark, null);

                        final EditText pass_titleET = dialogView.findViewById(R.id.pass_title);
                        final EditText pass_userNameET = dialogView.findViewById(R.id.pass_userName);
                        final EditText pass_userPWET = dialogView.findViewById(R.id.pass_userPW);
                        final EditText pass_URLET = dialogView.findViewById(R.id.pass_url);
                        final ImageView ib_icon = dialogView.findViewById(R.id.ib_icon);

                        final String decrypted_userName = mahEncryptor.decode(userName);
                        final String decrypted_userPW = mahEncryptor.decode(userPW);

                        pass_titleET.setText(title);
                        pass_userNameET.setText(decrypted_userName);
                        pass_userPWET.setText(decrypted_userPW);
                        pass_URLET.setText(url);

                        Button action_ok = dialogView.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    String input_pass_title = pass_titleET.getText().toString().trim();
                                    String input_pass_url = pass_URLET.getText().toString().trim();

                                    String encrypted_userName = mahEncryptor.encode(pass_userNameET.getText().toString().trim());
                                    String encrypted_userPW = mahEncryptor.encode(pass_userPWET.getText().toString().trim());

                                    db.update(Integer.parseInt(_id), HelperUnit.secString(input_pass_title), HelperUnit.secString(input_pass_url),  HelperUnit.secString(encrypted_userName), HelperUnit.secString(encrypted_userPW), pass_creation);
                                    initBookmarkList();
                                    hideKeyboard(activity);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    NinjaToast.show(context, R.string.toast_error);
                                }
                                hideBottomSheetDialog ();
                            }
                        });
                        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                        action_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideKeyboard(activity);
                                hideBottomSheetDialog ();
                            }
                        });
                        HelperUnit.switchIcon(activity, pass_creation, "pass_creation", ib_icon);
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);

                        ib_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    final String input_pass_title = pass_titleET.getText().toString().trim();
                                    final String input_pass_url = pass_URLET.getText().toString().trim();
                                    final String encrypted_userName = mahEncryptor.encode(pass_userNameET.getText().toString().trim());
                                    final String encrypted_userPW = mahEncryptor.encode(pass_userPWET.getText().toString().trim());

                                    hideBottomSheetDialog ();
                                    hideKeyboard(activity);

                                    bottomSheetDialog = new BottomSheetDialog(context);
                                    View dialogView = View.inflate(context, R.layout.dialog_edit_icon, null);

                                    GridView grid = dialogView.findViewById(R.id.grid_filter);
                                    GridItem_filter itemAlbum_01 = new GridItem_filter(sp.getString("icon_01", getResources().getString(R.string.color_red)), "icon_01", getResources().getDrawable(R.drawable.circle_red), "01");
                                    GridItem_filter itemAlbum_02 = new GridItem_filter(sp.getString("icon_02", getResources().getString(R.string.color_pink)), "icon_02", getResources().getDrawable(R.drawable.circle_pink), "02");
                                    GridItem_filter itemAlbum_03 = new GridItem_filter(sp.getString("icon_03", getResources().getString(R.string.color_purple)), "icon_03", getResources().getDrawable(R.drawable.circle_purple), "03");
                                    GridItem_filter itemAlbum_04 = new GridItem_filter(sp.getString("icon_04", getResources().getString(R.string.color_blue)), "icon_04", getResources().getDrawable(R.drawable.circle_blue), "04");
                                    GridItem_filter itemAlbum_05 = new GridItem_filter(sp.getString("icon_05", getResources().getString(R.string.color_teal)), "icon_05", getResources().getDrawable(R.drawable.circle_teal), "05");
                                    GridItem_filter itemAlbum_06 = new GridItem_filter(sp.getString("icon_06", getResources().getString(R.string.color_green)), "icon_06", getResources().getDrawable(R.drawable.circle_green), "06");
                                    GridItem_filter itemAlbum_07 = new GridItem_filter(sp.getString("icon_07", getResources().getString(R.string.color_lime)), "icon_07", getResources().getDrawable(R.drawable.circle_lime), "07");
                                    GridItem_filter itemAlbum_08 = new GridItem_filter(sp.getString("icon_08", getResources().getString(R.string.color_yellow)), "icon_08", getResources().getDrawable(R.drawable.circle_yellow), "08");
                                    GridItem_filter itemAlbum_09 = new GridItem_filter(sp.getString("icon_09", getResources().getString(R.string.color_orange)), "icon_09", getResources().getDrawable(R.drawable.circle_orange), "09");
                                    GridItem_filter itemAlbum_10 = new GridItem_filter(sp.getString("icon_10", getResources().getString(R.string.color_brown)), "icon_10", getResources().getDrawable(R.drawable.circle_brown), "10");
                                    GridItem_filter itemAlbum_11 = new GridItem_filter(sp.getString("icon_11", getResources().getString(R.string.color_grey)), "icon_11", getResources().getDrawable(R.drawable.circle_grey), "11");

                                    final List<GridItem_filter> gridList = new LinkedList<>();
                                    if (sp.getBoolean("filter_01", true)){
                                        gridList.add(gridList.size(), itemAlbum_01);
                                    }
                                    if (sp.getBoolean("filter_02", true)){
                                        gridList.add(gridList.size(), itemAlbum_02);
                                    }
                                    if (sp.getBoolean("filter_03", true)){
                                        gridList.add(gridList.size(), itemAlbum_03);
                                    }
                                    if (sp.getBoolean("filter_04", true)){
                                        gridList.add(gridList.size(), itemAlbum_04);
                                    }
                                    if (sp.getBoolean("filter_05", true)){
                                        gridList.add(gridList.size(), itemAlbum_05);
                                    }
                                    if (sp.getBoolean("filter_06", true)){
                                        gridList.add(gridList.size(), itemAlbum_06);
                                    }
                                    if (sp.getBoolean("filter_07", true)){
                                        gridList.add(gridList.size(), itemAlbum_07);
                                    }
                                    if (sp.getBoolean("filter_08", true)){
                                        gridList.add(gridList.size(), itemAlbum_08);
                                    }
                                    if (sp.getBoolean("filter_09", true)){
                                        gridList.add(gridList.size(), itemAlbum_09);
                                    }
                                    if (sp.getBoolean("filter_10", true)){
                                        gridList.add(gridList.size(), itemAlbum_10);
                                    }
                                    if (sp.getBoolean("filter_11", true)){
                                        gridList.add(gridList.size(), itemAlbum_11);
                                    }
                                    GridAdapter_filter gridAdapter = new xyz.johnzhang.frontier.View.GridAdapter_filter(context, gridList);
                                    grid.setAdapter(gridAdapter);
                                    gridAdapter.notifyDataSetChanged();

                                    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            db.update(Integer.parseInt(_id), HelperUnit.secString(input_pass_title), HelperUnit.secString(input_pass_url),  HelperUnit.secString(encrypted_userName), HelperUnit.secString(encrypted_userPW), gridList.get(position).getOrdinal());
                                            initBookmarkList();
                                            hideBottomSheetDialog ();
                                        }
                                    });

                                    grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                        @Override
                                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                            editFilterNames(gridList.get(position).getTitle(), gridList.get(position).getURL());
                                            return true;
                                        }
                                    });

                                    bottomSheetDialog.setContentView(dialogView);
                                    bottomSheetDialog.show();
                                    HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideBottomSheetDialog ();
                                    NinjaToast.show(context, R.string.toast_error);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        NinjaToast.show(context, R.string.toast_error);
                    }
                }
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void editFilterNames (String icon_string, final String icon_sp) {
        hideBottomSheetDialog ();
        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_edit_title, null);

        final EditText editText = dialogView.findViewById(R.id.dialog_edit);

        editText.setHint(R.string.dialog_title_hint);
        editText.setText(icon_string);

        Button action_ok = dialogView.findViewById(R.id.action_ok);
        action_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                if (text.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                } else {
                    sp.edit().putString(icon_sp, text).apply();
                    hideBottomSheetDialog ();
                    show_dialogFilter();
                }
            }
        });
        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(activity);
                hideBottomSheetDialog ();
            }
        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void show_dialogFilter() {
        hideBottomSheetDialog();

        open_bookmark.performClick();

        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_edit_icon, null);

        GridView grid = dialogView.findViewById(R.id.grid_filter);
        GridItem_filter itemAlbum_01 = new GridItem_filter(sp.getString("icon_01", getResources().getString(R.string.color_red)), "icon_01", getResources().getDrawable(R.drawable.circle_red), "01");
        GridItem_filter itemAlbum_02 = new GridItem_filter(sp.getString("icon_02", getResources().getString(R.string.color_pink)), "icon_02", getResources().getDrawable(R.drawable.circle_pink), "02");
        GridItem_filter itemAlbum_03 = new GridItem_filter(sp.getString("icon_03", getResources().getString(R.string.color_purple)), "icon_03", getResources().getDrawable(R.drawable.circle_purple), "03");
        GridItem_filter itemAlbum_04 = new GridItem_filter(sp.getString("icon_04", getResources().getString(R.string.color_blue)), "icon_04", getResources().getDrawable(R.drawable.circle_blue), "04");
        GridItem_filter itemAlbum_05 = new GridItem_filter(sp.getString("icon_05", getResources().getString(R.string.color_teal)), "icon_05", getResources().getDrawable(R.drawable.circle_teal), "05");
        GridItem_filter itemAlbum_06 = new GridItem_filter(sp.getString("icon_06", getResources().getString(R.string.color_green)), "icon_06", getResources().getDrawable(R.drawable.circle_green), "06");
        GridItem_filter itemAlbum_07 = new GridItem_filter(sp.getString("icon_07", getResources().getString(R.string.color_lime)), "icon_07", getResources().getDrawable(R.drawable.circle_lime), "07");
        GridItem_filter itemAlbum_08 = new GridItem_filter(sp.getString("icon_08", getResources().getString(R.string.color_yellow)), "icon_08", getResources().getDrawable(R.drawable.circle_yellow), "08");
        GridItem_filter itemAlbum_09 = new GridItem_filter(sp.getString("icon_09", getResources().getString(R.string.color_orange)), "icon_09", getResources().getDrawable(R.drawable.circle_orange), "09");
        GridItem_filter itemAlbum_10 = new GridItem_filter(sp.getString("icon_10", getResources().getString(R.string.color_brown)), "icon_10", getResources().getDrawable(R.drawable.circle_brown), "10");
        GridItem_filter itemAlbum_11 = new GridItem_filter(sp.getString("icon_11", getResources().getString(R.string.color_grey)), "icon_11", getResources().getDrawable(R.drawable.circle_grey), "11");

        final List<GridItem_filter> gridList = new LinkedList<>();
        if (sp.getBoolean("filter_01", true)){
            gridList.add(gridList.size(), itemAlbum_01);
        }
        if (sp.getBoolean("filter_02", true)){
            gridList.add(gridList.size(), itemAlbum_02);
        }
        if (sp.getBoolean("filter_03", true)){
            gridList.add(gridList.size(), itemAlbum_03);
        }
        if (sp.getBoolean("filter_04", true)){
            gridList.add(gridList.size(), itemAlbum_04);
        }
        if (sp.getBoolean("filter_05", true)){
            gridList.add(gridList.size(), itemAlbum_05);
        }
        if (sp.getBoolean("filter_06", true)){
            gridList.add(gridList.size(), itemAlbum_06);
        }
        if (sp.getBoolean("filter_07", true)){
            gridList.add(gridList.size(), itemAlbum_07);
        }
        if (sp.getBoolean("filter_08", true)){
            gridList.add(gridList.size(), itemAlbum_08);
        }
        if (sp.getBoolean("filter_09", true)){
            gridList.add(gridList.size(), itemAlbum_09);
        }
        if (sp.getBoolean("filter_10", true)){
            gridList.add(gridList.size(), itemAlbum_10);
        }
        if (sp.getBoolean("filter_11", true)){
            gridList.add(gridList.size(), itemAlbum_11);
        }

        GridAdapter_filter gridAdapter = new xyz.johnzhang.frontier.View.GridAdapter_filter(context, gridList);
        grid.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putString("filter_passBY", gridList.get(position).getOrdinal()).apply();
                initBookmarkList();
                hideBottomSheetDialog ();
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editFilterNames(gridList.get(position).getTitle(), gridList.get(position).getURL());
                return true;
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setCustomFullscreen(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        if (fullscreen) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    private AlbumController nextAlbumController(boolean next) {
        if (BrowserContainer.size() <= 1) {
            return currentAlbumController;
        }
        List<AlbumController> list = BrowserContainer.list();
        int index = list.indexOf(currentAlbumController);
        if (next) {
            index++;
            if (index >= list.size()) {
                index = 0;
            }
        } else {
            index--;
            if (index < 0) {
                index = list.size() - 1;
            }
        }
        return list.get(index);
    }
}
