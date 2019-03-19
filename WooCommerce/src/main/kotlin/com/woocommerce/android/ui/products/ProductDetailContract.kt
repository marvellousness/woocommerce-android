package com.woocommerce.android.ui.products

import com.woocommerce.android.ui.base.BasePresenter
import com.woocommerce.android.ui.base.BaseView
import org.wordpress.android.fluxc.model.WCProductModel

interface ProductDetailContract {
    interface Presenter : BasePresenter<View> {
        var remoteProductId: Long
        var product: WCProductModel?

        fun getProduct(remoteProductId: Long): WCProductModel?
        fun fetchProduct(remoteProductId: Long)
    }

    interface View : BaseView<Presenter> {
        fun showProduct(product: WCProductModel)
        fun showFetchProductError()

        fun hideProgress()
        fun showProgress()
    }
}