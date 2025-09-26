package dev.whysoezzy.pokemon

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dev.whysoezzy.pokemon", appContext.packageName)
    }

    @Test
    fun applicationIsInstantiated() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val applicationInfo = appContext.applicationInfo

        assertEquals("dev.whysoezzy.pokemon", applicationInfo.packageName)
    }
}
