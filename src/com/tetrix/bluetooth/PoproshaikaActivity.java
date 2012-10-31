package com.tetrix.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

public class PoproshaikaActivity extends Activity {

	private static final boolean D = true;
	// private static final String TAG = "BluetoothChat";
	/** Called when the activity is first created. */

	// Name of the connected device
	private String mConnectedDeviceName = null;

	public static final String DEVICE_NAME = "device_name"; // ��� ����������
	public static final String TOAST = "toast"; // ���������� ��� ������
												// ��������� �� �����

	private BluetoothAdapter mBluetoothAdapter = null;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_DISCONNECT_DEVICE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// ���������
	private static final byte GETBATTERYLEVEL = 0x0B;
	private static final byte MESSAGEWRITE = 0x09;
	private static final byte MESSAGEREAD = 0x13;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	private BluetoothChatService mChatService = null;
	// private ArrayAdapter<String> mConversationArrayAdapter;

	public TextView Name;
	public Button buttonScan;
	public boolean on;
	public MySurfaceView Surf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.main_layout);

		Surf = new MySurfaceView(this);
		linearlayout.addView(Surf);

		final Button foward = (Button) findViewById(R.id.foward);
		foward.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});

		foward.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('W');
				return false;
			}
		});

		final Button backward = (Button) findViewById(R.id.backward);
		backward.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		backward.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('S');
				return false;
			}
		});

		final Button left = (Button) findViewById(R.id.left);
		left.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		left.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('A');
				return false;
			}
		});

		final Button right = (Button) findViewById(R.id.right);
		right.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		right.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('D');
				return false;
			}
		});

		final Button fowardLeft = (Button) findViewById(R.id.fowardLeft);
		fowardLeft.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		fowardLeft.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('Q');
				return false;
			}
		});

		final Button fowardRight = (Button) findViewById(R.id.fowardRight);
		fowardRight.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		fowardRight.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('E');
				return false;
			}
		});

		final Button backwardLeft = (Button) findViewById(R.id.backwardLeft);
		backwardLeft.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		backwardLeft.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('Z');
				return false;
			}
		});

		final Button backwardRight = (Button) findViewById(R.id.backwardRight);
		backwardRight.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				sendMessage('T');
			}
		});
		backwardRight.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				sendMessage('C');
				return false;
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) // ���� �� ������
			{
				buttonScan.setMinWidth(buttonScan.getWidth());
				togleScan();
			}
		});

		Name = (TextView) findViewById(R.id.name);

		/*setupBluetooth();*/

	};

	public static void waiting(int n) {
		long t0, t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < n);
	}

	private void togleScan() {
		if (on) {
			sendMessage('H');
			buttonScan.setText(R.string.button_scan_off);
			on = false;
		} else {
			sendMessage('B');
			buttonScan.setText(R.string.button_scan_on);
			on = true;
		}
	}

	@Override
	protected void onDestroy() {
		if(mChatService != null)
			mChatService.stop();
		super.onDestroy();
	}

	private void setupBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// ���� ������� null, ������ Bluetooth �� ��������������
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth �� ��������", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

			int time = 0;
			while (mBluetoothAdapter.isEnabled() && time < 30) {
				waiting(1000);
				time++;
			}

			if (time == 30) {
				Toast.makeText(this, "Bluetooth �������� �� �������",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			} else {
				Toast.makeText(this, "Bluetooth �������", Toast.LENGTH_LONG)
						.show();
				mChatService = new BluetoothChatService(this, mHandler);
			}
		}
	}

	private void sendMessage(char msg) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			Log.e("sendMessage", "not connected");
			return;
		}

		byte[] send = { 6, 0, -128, 9, 0, 2, (byte) msg, 0 };

		mChatService.write(send);
		// Reset out string buffer to zero and clear the edit text field
		// }
	}

	private void startProgramm(char[] msg) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			Log.e("sendMessage", "not connected");
			return;
		}

		byte[] send;
		send = new byte[msg.length + 4];
		send[0] = (byte) (msg.length + 2);
		send[1] = 0;
		send[2] = -128;
		send[3] = 0;

		for (int i = 0; i < msg.length; i++)
			send[i + 4] = (byte) msg[i];
		send[msg.length + 3] = 0x00;

		mChatService.write(send);
		if (D)
			Log.i("Message", "sendMessage " + send[0] + " " + (char) msg[0]
					+ " " + (char) msg[1] + " " + (char) msg[2] + " "
					+ (char) msg[3] + " " + (char) msg[4] + " " + (char) msg[5]
					+ " " + (char) msg[6] + " " + (char) msg[7] + " "
					+ (char) msg[8] + " " + (char) msg[9]);
		// Reset out string buffer to zero and clear the edit text field
		// }
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i("Handler", "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					// setStatus(getString(R.string.title_connected_to,
					// mConnectedDeviceName));
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					// setStatus(R.string.title_connecting);
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					// setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				// byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				// String writeMessage = new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				switch (readBuf[3]) {
				case MESSAGEREAD:
					Surf.onResumeMySurfaceView(readBuf);
					break;
				}
				short i = (short) (0x0000 + (readBuf[1] << 8) + readBuf[0] + 32767);
				// Surf.onResumeMySurfaceView();

				// construct a string from the valid bytes in the buffer
				// String readMessage = new String(readBuf, 0, msg.arg1);
				// mConversationArrayAdapter.add(mConnectedDeviceName+":  " +
				// readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void GetRobotName() {
		
		// Name.setText("����� ����� ��� ������"); // ����������� �����������
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d("onActivityResult", "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_DISCONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				// Name.setText("Bluetooth �������");
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d("onActivityResult", "BT not enabled");
				Toast.makeText(this, "Bluetooth �� �����������",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		Name.setText(device.getName());
		mConnectedDeviceName = device.getName();
		// Attempt to connect to the device

		mChatService.connect(device, secure);
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d("ensureDiscoverable", "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.disconnect:
			// Launch the DeviceListActivity to see devices and do scan
			/*
			 * serverIntent = new Intent(this, DeviceListActivity.class);
			 * startActivityForResult(serverIntent,
			 * REQUEST_CONNECT_DEVICE_INSECURE);
			 */
			mChatService.stop();
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		case R.id.run_programm:
			// Ensure this device is discoverable by others
			// ensureDiscoverable();
			char[] name = { 'n', 'x', 't', 't', 't', '.', 'r', 'x', 'e', 0x00 };
			startProgramm(name);
			return true;
		}
		return false;
	}

}
