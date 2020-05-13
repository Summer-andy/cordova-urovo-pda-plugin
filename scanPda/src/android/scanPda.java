package org.apache.cordova.scanPda;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class scanPda extends CordovaPlugin
{
  private BroadcastReceiver mScanReceiver;
  private Activity activity;
  private ScanManager scanManager;
  private final static String SCAN_ACTION = "android.intent.ACTION_DECODE_DATA";// default
  // action

  @Override
  public boolean execute(String action, JSONArray args,
                         CallbackContext callbackContext) throws JSONException
  {
    Log.e("urovo", "------execute11：" + action);
    if (action.equals("coolMethod"))
    {
      String message = args.getString(0);
      if (TextUtils.isEmpty(message))
      {
        callbackContext.error("faild,args cannot be empty");
        return true;
      }
      if ("start".equals(args.get(0)))
      {
        startScan(message, callbackContext);
      }
      else if ("stop".equals(args.get(0)))
      {
        stopScan();
      }
      else
      {
        callbackContext.error("failed,Operation does not exist");
      }
      return true;
    }
    return false;
  }

  /**
   * open scanner
   *
   * @param message
   * @param callbackContext
   */
  private void startScan(final String message,
                         final CallbackContext callbackContext)
  {
    // TODO Auto-generated method stub
    // init scanner
    scanManager = new ScanManager();
    try
    {
      scanManager.openScanner();
    }
    catch (Exception e)
    {
      // TODO: handle exception
      e.printStackTrace();
      callbackContext.error("open scanner failed");
      return;
    }

    int[] index = new int[]
      { PropertyID.WEDGE_KEYBOARD_ENABLE, PropertyID.WEDGE_KEYBOARD_TYPE,
        PropertyID.GOOD_READ_BEEP_ENABLE };
    int[] value = new int[]
      { 0, 1, 1 };
    scanManager.setParameterInts(index, value);
    // register brocastReceiver
    IntentFilter filter = new IntentFilter();
    int[] idbuf = new int[]
      { PropertyID.WEDGE_INTENT_ACTION_NAME,
        PropertyID.WEDGE_INTENT_DATA_STRING_TAG };
    String[] value_buf = scanManager.getParameterString(idbuf);
    if (value_buf != null && !TextUtils.isEmpty(value_buf[0]))
    {
      filter.addAction(value_buf[0]);
    }
    else
    {
      filter.addAction(SCAN_ACTION);
    }
    activity = this.cordova.getActivity();
    if (activity != null)
    {
      mScanReceiver = new BroadcastReceiver()
      {
        @Override
        public void onReceive(Context context, Intent intent)
        {
          // TODO Auto-generated method stub
          byte[] barcode = intent.getByteArrayExtra("barcode");
          int barcodelen = intent.getIntExtra("length", 0);
          byte temp = intent.getByteExtra("barcodeType", (byte) 0);
          String barcodeStr = new String(barcode, 0, barcodelen);
          Log.e("urovo", "---barcode_type：" + temp);
          Log.e("urovo", "---scan_result：" + barcodeStr);
          coolMethod(barcodeStr, callbackContext);
        }
      };
      activity.registerReceiver(mScanReceiver, filter);
      // scanManager.startDecode();
    }
  }

  /**
   * stop scanner
   */
  private void stopScan()
  {
    // TODO Auto-generated method stub
     if (scanManager != null)
     {
//      scanManager.stopDecode();
//      scanManager.closeScanner();
     }
      if (activity != null)
      {
        Log.e("urovo", "---barcode_type：" + mScanReceiver);
        activity.unregisterReceiver(mScanReceiver);
      }
  }

  private void coolMethod(String message, CallbackContext callbackContext)
  {
    if (!TextUtils.isEmpty(message))
    {
      callbackContext.success(message);
    }
    else
    {
      callbackContext.error("scan error.");
    }
    stopScan();
  }
}
