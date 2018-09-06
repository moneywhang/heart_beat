package com.example.money.heart;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class bluetooth_40 {

    Timer mTimer;
    private BluetoothAdapter mBluetoothAdapter;//our local adapter
    private static final long SCAN_PERIOD = 1000; //5 seconds
    private static List<BluetoothDevice> mDevices;//discovered devices in range
    private BluetoothDevice mDevice; //external BLE device (Grove BLE module)
    private static BluetoothGatt mBluetoothGatt; //provides the GATT functionality for communication
    public static String DEVICE_NAME = "MLT-BT05";//BT05-A
    public static String DEVICE_adress = "C8:FD:19:44:07:9F";//BT05-A
    private static final String GROVE_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_RX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public boolean BLe_stus =false ,connect_stus =false;
    public static BluetoothGattService mBluetoothGattService; //service on mBlueoothGatt
    private Activity activity;

   public String rssi_string;
   public     String readdata_ble,readdata_ble1="100",readdata_ble4,readdata_ble5 ;
    public int rssi_int1;
    BluetoothDevice device;
    Analysis_value analysis_value;
    public boolean A_disable =false,A_connectstus=false;
    public static float [] blesetAry =new float[30];
    public static float [] blesetAry1 =new float[30];
    public static float [] blesetAry2 =new float[30];
    private  int blesetcount =0, blesetcount1 =0,blesetcount2=0;
    public static float blee,blee1,blee2;
   public static boolean drawstus =false,serch_stus =false;
    public static  List<String>mBleName;
    public static ArrayAdapter<String> btArrayAdapter;
    public bluetooth_40(Activity activity1, Activity activity) {
    }

    public bluetooth_40(Activity activity) {
        this.activity = activity;
        mDevices = new ArrayList<BluetoothDevice>();
        //Log.e("money","one1");
        final BluetoothManager mBluetoothManager = (BluetoothManager) activity.getSystemService(activity.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        searchForDevices();
        analysis_value =new Analysis_value();
        analysis_value.Bpm_data =Float.parseFloat(readdata_ble1);

    }


    public static void bluetoothset(String blumessage) {
        sendMessage(blumessage);
    }



    private static void statusUpdate(final String msg) {
        new Runnable() {
            @Override
            public void run() {
                Log.w("BLE", msg);
            }
        };

    }


    private void searchForDevices() {
        mTimer = new Timer();
        scanLeDevice();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                findGroveBLE();
                //Log.e("money","two");
            }
        }, SCAN_PERIOD);


    }
//----------------rssi

    public static void sendMessage(String _msg) {
        if (mBluetoothGattService == null)
            return;
       // Log.i("SS", "22");

       // statusUpdate("Finding Characteristic...");

        BluetoothGattCharacteristic gattCharacteristic =
                mBluetoothGattService.getCharacteristic(UUID.fromString(CHARACTERISTIC_TX));

        if (gattCharacteristic == null) {
          //  statusUpdate("Couldn't find TX characteristic: " + CHARACTERISTIC_TX);
            return;
        }

        //statusUpdate("Found TX characteristic: " + CHARACTERISTIC_TX);

        //statusUpdate("Sending message 'Hello Grove BLE'");

        String msg = _msg;

        byte b = 0x00;
        byte[] temp = msg.getBytes();
        byte[] tx = new byte[temp.length + 1];
        tx[0] = b;

        for (int i = 0; i < temp.length; i++)
            tx[i + 1] = temp[i];

        gattCharacteristic.setValue(tx);
        mBluetoothGatt.writeCharacteristic(gattCharacteristic);

    }
   public static void  sendMessage_byte(byte[] bbytes) {
     //  Log.i("jim","ble_sentin");
       if (mBluetoothGattService == null)
           return;
       BluetoothGattCharacteristic gattCharacteristic =
               mBluetoothGattService.getCharacteristic(UUID.fromString(CHARACTERISTIC_TX));

       if (gattCharacteristic == null) {
          // statusUpdate("Couldn't find TX characteristic: " + CHARACTERISTIC_TX);
           return;
       }
       byte b1 = 0x00;
       byte[] temp1 = bbytes;
       byte[] tx1 = new byte[temp1.length + 1];
       for (int i = 0; i < temp1.length; i++)
           tx1[i + 1] = temp1[i];
       gattCharacteristic.setValue(tx1);
       mBluetoothGatt.writeCharacteristic(gattCharacteristic);
   }

    private void scanLeDevice() {
        new Thread() {

            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);

                try {
                    Log.e("money","scan");
                    Thread.sleep(SCAN_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }.start();

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (device != null) {
                if (mDevices.indexOf(device) == -1)//to avoid duplicate entries
                {
                  /*  if (DEVICE_NAME.equals(device.getName())) {
                        mDevice = device;//we found our device!
                      //  Log.i(" money ", "Added " + device);
                       // Log.i(" money ", "Added " + device.getName() + ": " + device.getAddress());
                    }
                    mDevices.add(device);*/
                    if(!mDevices.contains(device)){
                        mDevices.add(device);
                        mBleName.add(device.getAddress());
                        btArrayAdapter.notifyDataSetChanged();
                       /* btArrayAdapter.notifyDataSetChanged();
                        btArrayAdapter.notifyDataSetChanged();
                        btArrayAdapter.notifyDataSetChanged();*/
                        serch_stus =true;
                        for(int i=0;i<mBleName.size();i++){
                            Log.i(" money ", "Added " +mBleName.get(i));
                        }

                    }
                 //   statusUpdate("Found device " + device.getName());
                }
            }
        }
    };
    public void Connectoutside(){

        if(serch_stus ==true){
            //收尋完成
            for(int i=0;i<mDevices.size();i++){
                //  Log.i(" money ", "get" +blearray.get(i));

                if(DEVICE_adress.equals(mDevices.get(i).getAddress())){
                    Log.i(" money ", "連接" +mDevices.get(i));
                    // mDevice = device;
                    mDevice = mDevices.get(i);
                    connectDevice();

                }
            }
        }
        //scanLeDevice();
        //mBluetoothGatt = device.connectGatt(activity, false, mGattCallback);

    }
    private void findGroveBLE() {
        if (mDevices == null || mDevices.size() == 0) {
           // Log.i("money","no dvicw");
           // statusUpdate("No BLE devices found");
            return;
        } else if (mDevice == null) {
            //Log.i("money","dvice null");
          //  statusUpdate("Unable to find Grove BLE");
            return;
        } else {
            //Log.i("money","find");
           // statusUpdate("Found Grove BLE V1");
            //statusUpdate("Address: " + mDevice.getAddress());
          //  connectDevice();
        }
    }

    private boolean connectDevice() {
         device = mBluetoothAdapter.getRemoteDevice(mDevice.getAddress());
        if (device == null) {
           // Log.i("money","device_null");
           // statusUpdate("Unable to connect");
            return false;
        }
        // directly connect to the device
        //statusUpdate("Connecting ...");
        mBluetoothGatt = device.connectGatt(activity, false, mGattCallback);
        BLe_stus =true;

        return true;
    }
    public void Ble_Disconnect(){
        mBluetoothGatt.disconnect();
        mBluetoothGatt.disconnect();
        mBluetoothGatt.disconnect();
        mBluetoothGatt.disconnect();
        A_connectstus =false;

    }
    public void Ble_Reconnect(){
        mBluetoothGatt = device.connectGatt(activity, false, mGattCallback);


    }

public void Read_Rssi(){
    mBluetoothGatt.readRemoteRssi();//rssi
}
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //Log.i("money","Connected");
                //statusUpdate("Connected");
                //statusUpdate("Searching for services");
                mBluetoothGatt.discoverServices();
                A_disable=false;
                A_connectstus =true;

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //statusUpdate("Device disconnected");
                //Log.i("money","disconnected");
                A_disable =true;
//                mBluetoothGatt.close();
                rssi_int1 =-60;
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();

                for (BluetoothGattService gattService : gattServices) {
                    //statusUpdate("Service discovered: " + gattService.getUuid());
                    if (GROVE_SERVICE.equals(gattService.getUuid().toString())) {
                     //   Log.i("money","communication Service");
                        mBluetoothGattService = gattService;
                      //  statusUpdate("Found communication Service");
                        sendMessage("");
                    }
                }
                BluetoothGattCharacteristic GattCharacteristic_RX = mBluetoothGattService.getCharacteristic(UUID.fromString(CHARACTERISTIC_RX));
                mBluetoothGatt.setCharacteristicNotification(GattCharacteristic_RX, true);
            } else {
               // statusUpdate("onServicesDiscovered received: " + status);
            }
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            //Log.i("onCharacteristicChanged",TAG);
            byte[] data = characteristic.getValue();
            try {
                String str = new String(data, "UTF-8");
               // statusUpdate(str);
              //  Log.i("jim", "Fin11111111d");
                //jim 讀取資料
              readdata_ble =str;
                spilt_data(readdata_ble);
               // Log.i("Neo", "readdata_ble" +readdata_ble);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

            super.onReadRemoteRssi(gatt, rssi, status);
            //this.rssi =rssi;
            rssi_string =""+ rssi;
            rssi_int1 =rssi;
        }
    };
    private  void spilt_data(String readuse){
        String[] split_ary = readuse.split(",");
        readdata_ble =split_ary[0];
        readdata_ble4=split_ary[1];
        readdata_ble5=split_ary[5];
        blee =Float.valueOf(readdata_ble);
        blee1 =Float.valueOf(readdata_ble4);
        blee2 =Float.valueOf(readdata_ble5);
        Log.i("jim","blee:  "+blee);
        if(blee>0 && blee<15){
            blee =0;
        }
        if(blee<0 &&blee>-80){
            blee =0;
        }

        if(blee<-100){
            blee*=2;
        }
        if(blee<-300){
            blee =-100;
        }
        if(blee>500){
            blee =200;
        }
        drawstus=false;
      //Log.i("jim","y軸:  "+blee);
      //  readdata_ble1 =split_ary[1];
        //analysis_value.Bpm_data =Float.parseFloat(readdata_ble1);
        //--------------------------------------------------------------
        blesetAry[blesetcount]=blee;
        blesetAry1[blesetcount1]=blee1;
        blesetAry2[blesetcount2]=blee2;
        blesetcount++;
        blesetcount1++;
        blesetcount2++;
        //Log.i("JIM","showary"+blesetAry.length);
        if(blesetcount==10){

            blesetcount =0;
            drawstus=true;
        }
        if(blesetcount1==29){

            blesetcount1 =0;

        }
        if(blesetcount2==29){

            blesetcount2 =0;
        }


    }





}



