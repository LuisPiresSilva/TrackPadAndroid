package net.luispiressilva.mousepad

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.text.format.Formatter
import android.util.Log
import java.lang.ref.WeakReference
import java.net.InetAddress

/**
 * Created by Luis Silva on 09/01/2019.
 */

class NetworkSniffTask(context: Context) : AsyncTask<Void, Void, Void>() {

    val TAG: String = "nstask"

    var mContextRef: WeakReference<Context> = WeakReference<Context>(context)


    override fun doInBackground(vararg params: Void?): Void? {
        try {
            var context = mContextRef.get()?.let {

                val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.getActiveNetworkInfo()
                val wm: WifiManager = it.getSystemService(Context.WIFI_SERVICE) as WifiManager


                val connectionInfo: WifiInfo = wm.getConnectionInfo();
                val ipAddress = connectionInfo.getIpAddress();
                val ipString = Formatter.formatIpAddress(ipAddress);


                Log.d(TAG, "activeNetwork: " + activeNetwork.toString())
                Log.d(TAG, "ipString: " + ipString)

                val prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1)
                Log.d(TAG, "prefix: " + prefix)

                for (i in 0..255) {
                    val testIp = prefix + i
                    Log.d(TAG, "testIP: " + testIp)

                    val address: InetAddress = InetAddress.getByName(testIp)
                    val reachable = address.isReachable(25)
                    val hostName = address.getCanonicalHostName()

                    if (reachable)
                        Log.i(TAG, "Host: $hostName ($testIp) is reachable!")
                }
            }

        } catch (t : Throwable) {
            Log.e(TAG, "Well that's not good.", t);
        }

        return null
    }

}