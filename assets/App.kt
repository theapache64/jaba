package $PACKAGE_NAME

import android.app.Application
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.googlefonts.GoogleFonts

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        TwinKill.init(
            TwinKill.Builder()
                .setDefaultFont(GoogleFonts.GoogleSansRegular)
                .build()
        )
    }
}