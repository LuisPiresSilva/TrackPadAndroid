package net.luispiressilva.mousepad

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private lateinit var mDetector: GestureDetectorCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        NetworkSniffTask(this).execute()

        button.setOnClickListener {
            mTcpClient.sendMessage(input.text.toString())
        }

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = GestureDetectorCompat(this, this)
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this)

        mDetector.setIsLongpressEnabled(true)



    }


    override fun onStart() {
        super.onStart()
        ConnectTask().execute()
    }

    override fun onStop() {
        super.onStop()
        mTcpClient.stopClient()
    }

    lateinit var mTcpClient : TcpClient


    inner class ConnectTask : AsyncTask<String, String, TcpClient>() {
        override fun doInBackground(vararg params: String?): TcpClient? {
            //we create a TCPClient object
            mTcpClient = TcpClient (object : TcpClient.OnMessageReceived {
                override fun messageReceived(message: String) {
                    //here the messageReceived method is implemented
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }


    override fun onShowPress(e: MotionEvent?) {
        Log.i("TEST","onShowPress")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {

        Log.i("TEST","click")
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.i("TEST","down")
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (e2 != null) {
            if (e2.pointerCount == 1) {
                Log.i("TEST","1 fingers scroll: x=" + distanceX + "  Y=" + distanceY)
            }
            if (e2.pointerCount == 2) {
                Log.i("TEST","2 fingers scroll: x=" + distanceX + "  Y=" + distanceY)
            }
            if (e2.pointerCount == 3) {
                Log.i("TEST","3 fingers scroll: x=" + distanceX + "  Y=" + distanceY)
            }
            if (e2.pointerCount == 4) {
                Log.i("TEST","4 fingers scroll: x=" + distanceX + "  Y=" + distanceY)
            }
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.i("TEST","onLongPress")

    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.i("TEST","onDoubleTap")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.i("TEST","onDoubleTapEvent")
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.i("TEST","onSingleTapConfirmed")
        return true
    }
}
