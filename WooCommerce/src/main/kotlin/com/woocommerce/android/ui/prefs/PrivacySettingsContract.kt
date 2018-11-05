package com.woocommerce.android.ui.prefs

import com.woocommerce.android.ui.base.BasePresenter
import com.woocommerce.android.ui.base.BaseView

interface PrivacySettingsContract {
    interface Presenter : BasePresenter<View> {
        fun getSendUsageStats(): Boolean
        fun setSendUsageStats(sendUsageStats: Boolean)
        fun getCrashReportingEnabled(): Boolean
        fun setCrashReportingEnabled(enabled: Boolean)
    }

    interface View : BaseView<Presenter> {
        fun showCookiePolicy()
        fun showPrivacyPolicy()
    }
}
