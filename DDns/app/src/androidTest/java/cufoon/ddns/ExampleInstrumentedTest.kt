package cufoon.ddns

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import cufoon.ddns.service.ModifyRecord
import cufoon.ddns.util.WifiTool

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("cufoon.ddns", appContext.packageName)
    }

    @Test
    fun testGetWifiAddress() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val systemIP = WifiTool.getSystemIP(appContext)
        Log.v("cufoon", systemIP)
        ModifyRecord.changeARecord(systemIP)
    }
}