package com.bonfire.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonfire.home.adapters.LightModeAdapter;
import com.bonfire.home.bottomsheets.ApplyingBottomSheet;
import com.bonfire.home.bottomsheets.CloseConnectionBottomSheet;
import com.bonfire.home.bottomsheets.SelectColorBottomSheet;
import com.bonfire.home.bottomsheets.SelectIntervalBottomSheet;
import com.bonfire.home.datas.LightMode;
import com.bonfire.home.utils.AlphaAnimation;
import com.bonfire.home.utils.BluetoothConnect;
import com.bonfire.home.utils.Font;
import com.bonfire.home.utils.FullscreenTheme;
import com.bonfire.home.utils.VoiceCommand;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener,
  BluetoothConnect.BluetoothConnectionListener {
  @BindView(R.id.rv_light_mode)
  RecyclerView recyclerViewLightMode;

  @BindView(R.id.ln_app_bar)
  LinearLayout linearLayoutAppBar;
  @BindView(R.id.fr_interval)
  FrameLayout frameLayoutInterval;
  @BindView(R.id.fr_bluetooth)
  FrameLayout frameLayoutBluetooth;
  @BindView(R.id.fr_apply)
  FrameLayout frameLayoutApply;
  @BindView(R.id.fr_led_color)
  FrameLayout frameLayoutLedColor;

  @BindView(R.id.card_done)
  CardView cardViewDone;
  @BindView(R.id.card_led_color)
  CardView cardViewLedColor;
  @BindView(R.id.card_interval)
  CardView cardViewInterval;

  @BindView(R.id.div_top_nested_scroll_view)
  View divTopNestedScrollView;
  @BindView(R.id.div_bottom_nested_scroll_view)
  View divBottomNestedScrollView;
  @BindView(R.id.elevation)
  View materialElevation;

  @BindView(R.id.tv_title)
  TextView textViewTitle;
  @BindView(R.id.tv_apply)
  TextView textViewApply;
  @BindView(R.id.tv_mode)
  TextView textViewMode;
  @BindView(R.id.tv_configuration)
  TextView textViewConfiguration;
  @BindView(R.id.tv_bluetooth_title)
  TextView textViewBluetoothTitle;
  @BindView(R.id.tv_bluetooth_subtitle)
  TextView textViewBluetoothSubtitle;
  @BindView(R.id.tv_interval)
  TextView textViewInterval;

  @BindView(R.id.img_mic)
  ImageView imageViewMic;

  @BindView(R.id.nested_scroll_view)
  NestedScrollView nestedScrollView;

  private LightModeAdapter lightModeAdapter;
  private CloseConnectionBottomSheet closeConnectionBottomSheet;
  private SelectIntervalBottomSheet selectIntervalBottomSheet;
  private ApplyingBottomSheet applyingBottomSheet;
  private SelectColorBottomSheet selectColorBottomSheet;
  private BluetoothConnect bluetoothConnect;
  private String bluetoothAddress = "00:21:13:01:41:1D"; // default
  private VoiceCommand voiceCommand;

  private int bluetoothStatus = 0;
  private String bluetoothErrorMessage = "";
  private static final int BLUETOOTH_DISABLED = 0;
  private static final int BLUETOOTH_ENABLED = 3;
  private static final int BLUETOOTH_DISCONNECTED = 1;
  private static final int BLUETOOTH_CONNECTED = 2;
  private static final int BLUETOOTH_TURNING_ON = 4;
  private static final int BLUETOOTH_TURNING_OFF = 5;
  private static final int BLUETOOTH_CONNECTING = 6;
  private static final int BLUETOOTH_CONNECTION_ERROR = 7;

  public static int selectedInterval = 10; // default 10ms
  private String selectedLedPin = "";
  private String selectedLedColor = "";
  private String selectedRed = "0"; // will
  private String selectedGreen = "0";
  private String selectedBlue = "0";
  public static String selectedLedMode = LightMode.OFF; // --> default mode off
  private boolean isApplying = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.a();
    this.aa();
    this.b();
    this.c();
    this.d();
  }

  private void a() {
    ButterKnife.bind(this);
    FullscreenTheme.initialize(this);
    FullscreenTheme.initializeAppBar(
        this.linearLayoutAppBar,
        88,
        this.divTopNestedScrollView,
        this.divBottomNestedScrollView,
        this.cardViewDone
    );

    this.lightModeAdapter = new LightModeAdapter(this);
    this.applyingBottomSheet = new ApplyingBottomSheet();
    this.selectIntervalBottomSheet = new SelectIntervalBottomSheet();
    this.closeConnectionBottomSheet = new CloseConnectionBottomSheet();
    this.selectColorBottomSheet = new SelectColorBottomSheet();
    this.bluetoothConnect = new BluetoothConnect(this);
    this.voiceCommand = new VoiceCommand(this);

    this.lightModeAdapter.setModeChangedListener(this::e);
    this.selectIntervalBottomSheet.setSelectIntervalListener(this::g);
    this.closeConnectionBottomSheet.setCloseListener(this::h);
    this.selectColorBottomSheet.setColorSelectedListener(this::i);
    BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
          final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
          switch (state) {
            case BluetoothAdapter.STATE_OFF:
              bluetoothStatus = BLUETOOTH_DISABLED;
              break;

            case BluetoothAdapter.STATE_ON:
              bluetoothStatus = BLUETOOTH_ENABLED;
              break;

            case BluetoothAdapter.STATE_TURNING_ON:
              bluetoothStatus = BLUETOOTH_TURNING_ON;
              break;

            case BluetoothAdapter.STATE_TURNING_OFF:
              bluetoothStatus = BLUETOOTH_TURNING_OFF;
              break;
          }
          ab();
        }
      }
    };

    IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    this.registerReceiver(bluetoothBroadcastReceiver, intentFilter);
  }

  private void aa() {
    String a = "";
    String b = "";
    if (this.bluetoothConnect.isBluetoothActivated()) {
      a = getString(R.string.disconnected);
      b = getString(R.string.open_bluetooth_connection);
      this.bluetoothStatus = BLUETOOTH_DISCONNECTED;
    } else {
      a = getString(R.string.bluetooth_disabled);
      b = getString(R.string.bluetooth_tap_to_enable);
      this.bluetoothStatus = BLUETOOTH_DISABLED;
    }
    this.textViewBluetoothTitle.setText(a);
    this.textViewBluetoothSubtitle.setText(b);
  }

  private void ab() {
    String a = "";
    String b = "";

    switch (this.bluetoothStatus) {
      case BLUETOOTH_CONNECTED :
        a = getString(R.string.connected);
        b = getString(R.string.device_address).replace("%address%", this.bluetoothAddress);
        break;

      case BLUETOOTH_ENABLED :
      case BLUETOOTH_DISCONNECTED :
        a = getString(R.string.disconnected);
        b = getString(R.string.open_bluetooth_connection);
        break;

      case BLUETOOTH_DISABLED :
        a = getString(R.string.bluetooth_disabled);
        b = getString(R.string.bluetooth_tap_to_enable);
        break;

      case BLUETOOTH_TURNING_ON :
        a = getString(R.string.turning_on_bluetooth);
        b = getString(R.string.please_wait);
        break;

      case BLUETOOTH_TURNING_OFF :
        a = getString(R.string.turning_off_bluetooth);
        b = getString(R.string.please_wait);
        break;

      case BLUETOOTH_CONNECTING :
        a = getString(R.string.connecting);
        b = getString(R.string.please_wait);
        break;

      case BLUETOOTH_CONNECTION_ERROR :
        a = getString(R.string.connection_error);
        b = this.bluetoothErrorMessage;
        break;
    }

    this.textViewBluetoothTitle.setText(a);
    this.textViewBluetoothSubtitle.setText(b);
  }

  private void b() {
    this.recyclerViewLightMode.setAdapter(this.lightModeAdapter);
    this.recyclerViewLightMode.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    this.lightModeAdapter.notifyDataSetChanged();

    this.nestedScrollView.setOnScrollChangeListener(this);
    this.frameLayoutBluetooth.setOnClickListener(view -> this.f());
    this.frameLayoutApply.setOnClickListener(view -> {
      if (bluetoothStatus == BLUETOOTH_CONNECTED)
        this.apply();
    });
  }

  private void c() {
    Font.setGilroyBold(
        this.textViewTitle,
        this.textViewMode,
        this.textViewConfiguration
    );
    Font.setSofiaProLight(
        this.textViewApply
    );
    Font.setBoldStyle(
        this.textViewApply
    );
  }

  private void d() {
    this.imageViewMic.setOnClickListener(view -> this.openMic());

    this.frameLayoutInterval.setOnClickListener(view -> {
      if (this.selectIntervalBottomSheet.getDialog() == null) {
        this.selectIntervalBottomSheet.show(this.getSupportFragmentManager(), "select-interval-bottom-sheet");
      }
    });

    this.frameLayoutLedColor.setOnClickListener(view -> {
      if (this.selectColorBottomSheet.getDialog() == null) {
        this.selectColorBottomSheet.show(this.getSupportFragmentManager(), "select-color-bottom-sheet");
      }
    });
  }

  private void e(String lightMode) {
    this.textViewConfiguration.setVisibility(
        lightMode.equals(LightMode.OFF) ? View.GONE : View.VISIBLE);
    this.cardViewLedColor.setVisibility(
        lightMode.equals(LightMode.OFF) || lightMode.equals(LightMode.COLOR_CYCLE) ?
            View.GONE : View.VISIBLE);
    this.cardViewInterval.setVisibility(
        lightMode.equals(LightMode.OFF) || lightMode.equals(LightMode.STATIC) ?
            View.GONE : View.VISIBLE);
    this.selectedLedMode = lightMode;
  }

  private void f() {
    switch (this.bluetoothStatus) {
      case BLUETOOTH_DISABLED :
        if (this.bluetoothConnect != null)
          this.bluetoothConnect.activateBluetooth();
        break;

      case BLUETOOTH_ENABLED :
      case BLUETOOTH_CONNECTION_ERROR :
      case BLUETOOTH_DISCONNECTED :
        this.bluetoothStatus = BLUETOOTH_CONNECTING;
        this.ab();
        this.bluetoothConnect.startConnection(this, this.bluetoothAddress, "bluetooth-connect");
        break;

      case BLUETOOTH_CONNECTED :
        if (this.closeConnectionBottomSheet.getDialog() == null)
          this.closeConnectionBottomSheet.show(this.getSupportFragmentManager(), "close-connection-bottom-sheet");
        break;
    }
  }

  private void g(int interval) {
    selectedInterval = interval;
    String intervalText = String.valueOf(selectedInterval).concat(" ms");
    this.textViewInterval.setText(intervalText);
  }

  private void h(int result) {
    if (result == -1) {
      // close connection
      if (this.bluetoothConnect != null)
        this.bluetoothConnect.stopConnection(this, "bluetooth-connect");
    }
  }

  private void i(String result) {
    if (result.trim().length() == 1) {
      this.selectedLedPin = result;
    } else {
      this.selectedLedColor = result;
    }
  }

  private void openMic() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PERMISSION_DENIED)
      this.allowPermission();
    if (this.voiceCommand != null)
      this.voiceCommand.setListener(this::parseMic).startListening();
  }

  private void allowPermission() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1111);
  }

  private ArrayList<String> micDatas = new ArrayList<>();
  private void parseMic(ArrayList<String> data) {
    this.micDatas = data;

    String turnOff = "matikan lampu;matikan;turn off;power off;off";
    String turnOn = "hidupkan lampu;hidupkan;turn on;power on;on";

    String modeStatic = "hidupkan lampu statik;hidupkan lampu static;turn on static;" +
        "power on static;on static;static;statik;mode static; mode statik";
    String breathingMode = "hidupkan lampu breathing;hidupkan lampu breathing;turn on breathing;" +
        "power on breathing;on breathing;breathing;mode breathing";
    String colorCycleMode = "hidupkan lampu color cycle;hidupkan lampu color cycle;turn on color cycle;" +
        "power on color cycle;on color cycle;color cycle;mode color cycle";
    String strobingMode = "hidupkan lampu strobing;hidupkan lampu strobing;turn on strobing;" +
        "power on strobing;on strobing;strobing;mode strobing";

    String interval = "interval;jangka waktu;durasi";

    if (this._pM(turnOff)) {
      selectedLedMode = LightMode.OFF;
      this.apply();
      Log.d("status", "turn off");
    } else if (_pM(modeStatic)) {
      selectedLedMode = LightMode.STATIC;
      this.apply();
      Log.d("status", "static");
    } else if (_pM(breathingMode)) {
      selectedLedMode = LightMode.BREATHING;
      this.apply();
      Log.d("status", "breathing");
    } else if (_pM(colorCycleMode)) {
      selectedLedMode = LightMode.COLOR_CYCLE;
      this.apply();
      Log.d("status", "color cycle");
    } else if (_pM(strobingMode)) {
      selectedLedMode = LightMode.STROBING;
      this.apply();
      Log.d("status", "strobing");

    } else if (parseInterval(interval)) {
      this.apply();
    } else {
      if (this.micDatas != null)
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
      Log.d("status", "error parse");
    }
  }

  private boolean parseInterval(String queryData) {
    String[] queries = queryData.split(";");
    boolean state = false;
    if (this.micDatas != null) {
      for (String query : queries) {
        for (String data : this.micDatas) {
          if (data.contains(query)) {
            String result = data.replace(query, "");
            String interval = result.replaceAll("[^0-9]", "");
            selectedInterval = Integer.parseInt(interval);
            state = true;
            break;
          }
        }
      }
    }
    return state;
  }

  private boolean _pM(String a) {
    String[] _a = a.split(";");
    boolean _b = false;
    if (this.micDatas != null) {
      for (String data : _a) {
        for (String data2 : this.micDatas) {
          if (data2.toLowerCase().contains(data.toLowerCase())) {
            _b = true;
            break;
          }
        }
      }
    }
    return _b;
  }

  private void apply() {
    String mode = "";
    String data = "";
    switch (selectedLedMode) {
      case LightMode.OFF :
        mode = "off;";
        data = mode;
        break;

      case LightMode.STATIC :
        // --> static;red:38,green:50,,blue:56
        mode = "static;";
        data = mode + this.selectedLedColor;
        break;

      case LightMode.BREATHING :
        // --> breathing;pin:0,interval:5
        mode = "breathing;";
        data = mode + "pin:" + this.selectedLedPin + ",interval:" + selectedInterval;
        break;

      case LightMode.COLOR_CYCLE :
        // --> colorCycle;interval:5
        mode = "colorCycle;";
        data = mode + "interval:" + selectedInterval;
        break;

      case LightMode.STROBING :
        // --> strobing;pin:0,interval:5
        mode = "strobing;";
        data = mode + "pin:" + this.selectedLedPin + ",interval:" + selectedInterval;
        break;
    }
    if (this.bluetoothStatus == BLUETOOTH_CONNECTED) {
      Log.d("data", data);
      this.bluetoothConnect.sendData(this, data, "bluetooth-apply");
      this.isApplying = true;
      this.applyingBottomSheet.show(this.getSupportFragmentManager(), "applying-bottom-sheet");
    }
  }

  @Override
  public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    if (scrollY > oldScrollY) {
      // scroll down
      new AlphaAnimation().animateAlpha(this.materialElevation, true);
      new AlphaAnimation().animateAlpha(this.cardViewDone, false);
    } else {
      // scroll up
      new AlphaAnimation().animateAlpha(this.cardViewDone, true);
    }
    if (scrollY == 0) {
      // reach top
      new AlphaAnimation().animateAlpha(this.materialElevation, false);
    }
  }

  @Override
  public void onConnected(String tag, HashMap<String, Object> deviceData) {
    bluetoothStatus = BLUETOOTH_CONNECTED;
    ab();
  }

  @Override
  public void onDataReceived(String tag, byte[] bytes, int a) {
    String data = new String(bytes, StandardCharsets.UTF_8);
    if (data.trim().length() > 0) {
      if (this.isApplying) {
        if (data.equals("0"))
          Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        if (this.applyingBottomSheet.getDialog() != null)
          this.applyingBottomSheet.dismiss();
        this.isApplying = false;
      }
    }
  }

  @Override
  public void onDataSent(String tag, byte[] data) {

  }

  @Override
  public void onConnectionError(String tag, String connectionState, String message) {
    bluetoothStatus = BLUETOOTH_CONNECTION_ERROR;
    bluetoothErrorMessage = message;
    ab();
    if (!this.bluetoothConnect.isBluetoothActivated()) {
      bluetoothStatus = BLUETOOTH_DISABLED;
      this.ab();
    }
  }

  @Override
  public void onConnectionStopped(String tag) {
    bluetoothStatus = BLUETOOTH_DISCONNECTED;
    ab();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 1111) {
      if (grantResults[0] == PERMISSION_GRANTED)
        this.openMic();
    }
  }
}
