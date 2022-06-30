package com.example.lowerlimbexercise;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.example.android.bluetoothchat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//import com.example.android.common.logger.Log;


/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    String color;
    double angle = 0.0;
    boolean getAngle = false;
    private boolean mStartMeasure = false;
    private boolean bSetInitial ;

    static int devicenumber_righttoe = 0;
    static int devicenumber_lefttoe = 1;


    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    //private EditText mOutEditText;
    //private Button mSendButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatServicerighttoe = null;
    private BluetoothChatService mChatServicelefttoe = null;

    //移動角度
    double mmoveanglerighttoe;
    double mmoveanglelefttoe;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //System.out.println("++BluetoothChatFragment:onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter


            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                FragmentActivity activity = getActivity();   //これでActivityがとれる
                Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
                activity.finish();
            }

        bSetInitial = FALSE;


    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else {
                setupChat();
                if (mChatServicerighttoe == null) {
                    setupChatforrighttoe();
                }
                if(mChatServicelefttoe == null){
                    setupChatforlefttoe();
                }

            }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatServicerighttoe != null) {
            mChatServicerighttoe.stop();
        }
        if (mChatServicelefttoe != null) {
            mChatServicelefttoe.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatServicerighttoe != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatServicerighttoe.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatServicerighttoe.start();


            }

        }

        if (mChatServicelefttoe != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatServicelefttoe.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatServicelefttoe.start();


            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //System.out.println("++BluetoothChatFragment::onCreateView()");
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //System.out.println("++BluetoothChatFragment::onViewCreated()");
        mConversationView = (ListView) view.findViewById(R.id.in);
        //mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        //mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
       // Log.d(TAG, "setupChat()");
       // System.out.println("++BluetoothChatFragment::setupChat() ");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);


        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
       // System.out.println("--BluetoothChatFragment::setupChat() ");

    }

    private void setupChatforrighttoe() {
        //Log.d(TAG, "setupChatforrighttoe()");
        //System.out.println("++BluetoothChatFragment::setupChatforrighttoe() ");

        // Initialize the BluetoothChatService to perform bluetooth connections
       // System.out.println("new BluetoothChatService ");
        mChatServicerighttoe = new BluetoothChatService(getActivity(), mHandler,devicenumber_righttoe);


       // System.out.println("--BluetoothChatFragment::setupChatforrighttoe() ");

    }

    private void setupChatforlefttoe() {
        //Log.d(TAG, "setupChatforrighttoe()");
        //System.out.println("++BluetoothChatFragment::setupChatforrighttoe() ");



        // Initialize the BluetoothChatService to perform bluetooth connections
        //System.out.println("new BluetoothChatService ");
        mChatServicelefttoe = new BluetoothChatService(getActivity(), mHandler,devicenumber_lefttoe);


        //System.out.println("--BluetoothChatFragment::setupChatforrighttoe() ");

    }


    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything

        //System.out.println("BluetoothChatFragment:sendMessage("+message+")");
        if (mChatServicerighttoe.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            if (message.equals("計測開始")) {//計測開始コマンド
                // Get the message bytes and tell the BluetoothChatService to write
                MeasurStart();
            }
            if (message.equals("角度取得")) {
                getAngle = true;
            }
            if (message.equals("クォータニオン")) {
                try {
                    byte[] send = message.getBytes("UTF-8");
                    byte[] command = {(byte) 0x9A, (byte) 0x37,
                            (byte) 0x04, (byte) 0x01, (byte) 0x01,
                            (byte) 0xA9};

                    mChatServicerighttoe.write(send);
                    mChatServicerighttoe.write(command);
                    mChatServicelefttoe.write(send);
                    mChatServicelefttoe.write(command);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (message.equals("計測終了")) {//計測終了コマンド
                MeasurStop();

            }

            if(message.equals("初期値取得")){
              if(!(mChatServicerighttoe == null)) mChatServicerighttoe.setInitalPosition();
                if(!(mChatServicelefttoe == null)) mChatServicelefttoe.setInitalPosition();
            }
            if(message.equals("初期値取得終了")){
                if(!(mChatServicerighttoe == null)) mChatServicerighttoe.InitalPositionfinish();
                if(!(mChatServicelefttoe == null)) mChatServicelefttoe.InitalPositionfinish();
            }

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
        //System.out.println("sendMessage最後まで完了");
    }


    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener //今のところ動いている可能性は低い
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
               // System.out.println("textview.oneditactionlistenerからのメッセージ");
               // System.out.println(message);
                sendMessage(message);
            }
            return true;
        }
    };


    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        /*long startMilli = System.currentTimeMillis();
        long times1 = 0;        //計測開始時間
        long times2 = 0;        //現在の時間
        long times3 = 0;        //処理時間*/
        private int count;

        @Override
        public void handleMessage(Message msg) {
            //System.out.println("++BluetoothChatFragment::handleMessage(msg.what="+msg.what+",msg.arg1="+msg.arg1+")");
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    //BluetoothChatServiceに移動
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case Constants.MESSAGE_CLICK:


                    //底屈のピークを取得した
                    FragmentActivity factivity = getActivity();
                    if (null == activity) {
                        return;
                    }
                    if (msg.arg1 == devicenumber_righttoe) {
                        //移動角度取得
                        ((AncleExerciseActivity) factivity).setMoveangle(devicenumber_righttoe,mChatServicerighttoe.getMmoveangle(devicenumber_righttoe));
                        ((AncleExerciseActivity) factivity).rightAncleClick();
                    }
                    if (msg.arg1 == devicenumber_lefttoe){
                        ((AncleExerciseActivity) factivity).setMoveangle(devicenumber_lefttoe,mChatServicelefttoe.getMmoveangle(devicenumber_lefttoe));
                        ((AncleExerciseActivity)factivity).leftAncleClick();
                    }

                    break;

                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("++BluetoothChatFragment::onActivityResult(requestCode="+requestCode+")");
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                   /// connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                   // connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * //@param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * //@param secure Socket Security type - Secure (true) , Insecure (false)
     */
   /* private void connectDevice(Intent data, boolean secure) {//接続時使用確認
        System.out.println("++BluetoothChatFragment::connectDevice()");
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);//bluetooth接続時のアドレス
        System.out.println("開発者メッセージ:" + address);
        // Get the BluetoothDevice object
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:07:80:48:0E:D6");
        // Attempt to connect to the device
        mChatServicerighttoe.connect(device, secure);
    }

    //試しに追加
    private void connectDevice(boolean secure) {//接続時使用確認
        System.out.println("++BluetoothChatFragment::connectDevice()");
        // Get the BluetoothDevice object
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:07:80:48:0E:D6");
        BluetoothDevice device1 = mBluetoothAdapter.getRemoteDevice("00:07:80:48:0C:80");
        // Attempt to connect to the device
        mChatServicerighttoe.connect(device, secure);
        mChatServicelefttoe.connect(device1, secure);
    }*/




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //System.out.println("++BluetoothChatFragment::onCreateOptionsMenu()");
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //System.out.println("++BluetoothChatFragment::onOptionsItemSelected()");
        String deviceaddress = ((AncleExerciseActivity)getActivity()).getsensoraddress(devicenumber_righttoe);
        String deviceaddresslefttoe = ((AncleExerciseActivity)getActivity()).getsensoraddress(devicenumber_lefttoe);

        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceaddress);
        BluetoothDevice device1 = mBluetoothAdapter.getRemoteDevice(deviceaddresslefttoe);
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:07:80:48:0E:D6");
        //BluetoothDevice device1 = mBluetoothAdapter.getRemoteDevice("00:07:80:48:0C:80");
        // Attempt to connect to the device
        mChatServicerighttoe.connect(device, true);
        mChatServicelefttoe.connect(device1, true);

        return false;
    }

    public boolean MeasurStart(){
       // System.out.println("++BluetoothChatFragment::MeasurStart()");
        if (mChatServicerighttoe.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return FALSE;
        }

        // Get the message bytes and tell the BluetoothChatService to write
        try {
            String message = "計測開始";
            byte[] send = message.getBytes("UTF-8");
            byte[] command = {(byte) 0x9A, (byte) 0x13,
                    (byte) 0x00, (byte) 0x13, (byte) 0x09, (byte) 0x14,  (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x13, (byte) 0x09, (byte) 0x14,  (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x89};

            if(!(mChatServicerighttoe == null)) {
                mChatServicerighttoe.write(send);
                mChatServicerighttoe.write(command);
            }
            if(!(mChatServicelefttoe == null)) {
                mChatServicelefttoe.write(send);
                mChatServicelefttoe.write(command);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        mStartMeasure = TRUE;
        return TRUE;
    }

    public boolean MeasurStop(){
        try {
            String message ="計測終了";
            byte[] send = message.getBytes("UTF-8");
            byte[] command = {(byte) 0x9A, (byte) 0x15, (byte) 0x00, (byte) 0x8f};

            mChatServicerighttoe.write(send);
            mChatServicerighttoe.write(command);
            mChatServicelefttoe.write(send);
            mChatServicelefttoe.write(command);
        } catch (Exception e) {
            System.out.println(e);
        }
        mStartMeasure= FALSE;
        return TRUE;
    }



    public void callFromOut(){

        System.out.println("callFromOut!!");
    }

    //センサ初期位置取得要求
    public void setDefaultPosition(boolean bstart){
        //System.out.println("++BluetoothChatFragment::setDefaultPosition");
        String message="";
        if(bstart == true)
            message = "初期値取得";
        else
            message = "初期値取得終了";
        // System.out.println("setupChat内のmsendButton.setOnClickListenerからのメッセージ");
        //System.out.println(message);
        sendMessage(message);
    }

    public void connectDevices(){
        //System.out.println("++BluetoothChatFragment::connectDevices");
        sendMessage("計測開始");
    }

    public void stopMeasurement(){
        //System.out.println("++BluetoothChatFragment::stopMeasurement");
        sendMessage("計測終了");
    }
}