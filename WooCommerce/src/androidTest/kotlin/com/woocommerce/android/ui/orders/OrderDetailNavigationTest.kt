package com.woocommerce.android.ui.orders

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.woocommerce.android.R
import com.woocommerce.android.helpers.WCMatchers
import com.woocommerce.android.ui.TestBase
import com.woocommerce.android.ui.main.MainActivityTestRule
import com.woocommerce.android.util.DateUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.wordpress.android.fluxc.model.SiteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@LargeTest
class OrderDetailNavigationTest : TestBase() {
    @Rule
    @JvmField var activityTestRule = MainActivityTestRule()

    val dateFormat by lazy { DateTimeFormatter.ofPattern("YYYY-mm-dd'T'hh:mm:ss", Locale.getDefault()) }

    @Before
    override fun setup() {
        super.setup()
        // Bypass login screen and display dashboard
        activityTestRule.launchMainActivityLoggedIn(null, SiteModel())

        // Make sure the bottom navigation view is showing
        activityTestRule.activity.showBottomNav()

        // Click on Orders tab in the bottom bar
        Espresso.onView(ViewMatchers.withId(R.id.orders)).perform(ViewActions.click())
    }

    @Test
    fun verifyOrderDetailCardViewPopulatedSuccessfully() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail()
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card heading matches this format:
        // #1 Jane Masterson
        onView(withId(R.id.orderStatus_orderNum)).check(matches(
                withText(appContext.getString(
                R.string.orderdetail_orderstatus_heading,
                mockWCOrderModel.number, mockWCOrderModel.billingFirstName, mockWCOrderModel.billingLastName)))
        )

        // check if order card date created matches this format:
        // Created 8/12/2017 at 4:11 PM:
        val todayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, todayDateString))))
    }

    @Test
    fun verifyOrderDetailCardDateCreatedTodayView() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail(
                LocalDateTime.now().format(dateFormat)
        )
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card created date is displayed in this format:
        // Created <friendly date category> at <time> message, for Today:
        val todayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, todayDateString))))
    }

    @Test
    fun verifyOrderDetailCardDateCreatedYesterdayView() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail(
                LocalDateTime.now().minusDays(1).format(dateFormat)
        )
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card created date is displayed in this format:
        // Created <friendly date category> at <time> message, for Yesterday:
        val yDayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, yDayDateString))))
    }

    @Test
    fun verifyOrderDetailCardDateCreatedTwoDaysView() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail(
                LocalDateTime.now().minusDays(3).format(dateFormat)
        )
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card created date is displayed in this format:
        // Created <friendly date category> at <time> message, for Older than 2 days:
        val yDayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, yDayDateString))))
    }

    @Test
    fun verifyOrderDetailCardDateCreatedOlderThanAWeekView() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail(
                LocalDateTime.now().minusWeeks(2).format(dateFormat)
        )
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card created date is displayed in this format:
        // Created <friendly date category> at <time> message, for Older than a week:
        val yDayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, yDayDateString))))
    }

    @Test
    fun verifyOrderDetailCardDateCreatedOlderThanAMonthView() {
        // add mock data to order detail screen
        val mockWCOrderModel = WcOrderTestUtils.generateOrderDetail(
                LocalDateTime.now().minusMonths(2).format(dateFormat)
        )
        activityTestRule.setOrderDetailWithMockData(mockWCOrderModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order card created date is displayed in this format:
        // Created <friendly date category> at <time> message, for Older than a month:
        val yDayDateString = DateUtils.getFriendlyShortDateAtTimeString(
                appContext,
                mockWCOrderModel.dateCreated
        )
        onView(withId(R.id.orderStatus_created)).check(
                matches(withText(appContext.getString(R.string.orderdetail_orderstatus_created, yDayDateString))))
    }

    @Test
    fun verifyOrderDetailCardPendingStatusLabelView() {
        // add mock data to order detail screen
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail())

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText("Pending"))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_pending_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_pending_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardProcessingStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("Processing")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_processing_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_processing_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardOnHoldStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("On Hold")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_hold_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_hold_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardCompletedStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("Completed")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_completed_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_completed_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardCancelledStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("Cancelled")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_cancelled_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_cancelled_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardRefundedStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("Refunded")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_refunded_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_refunded_bg))
        )
    }

    @Test
    fun verifyOrderDetailCardFailedStatusLabelView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderStatusDetail("Failed")
        activityTestRule.setOrderDetailWithMockData(WcOrderTestUtils.generateOrderDetail(), wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // check if order status label is displayed correctly
        // Check if order status label name, label text color, label background color is displayed correctly
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagText(wcOrderStatusModel.label))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagTextColor(appContext, R.color.orderStatus_failed_text))
        )
        onView(withId(R.id.orderStatus_orderTags)).check(
                matches(WCMatchers.withTagBackgroundColor(appContext, R.color.orderStatus_failed_bg))
        )
    }

    @Test
    fun verifyOrderDetailNotesCardEmptyView() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderDetail()
        activityTestRule.setOrderDetailWithMockData(wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // customerNote is empty so Hide Customer Note card
        onView(withId(R.id.orderDetail_customerNote)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun verifyOrderDetailNotesCardViewIsDisplayed() {
        // add mock data to order detail screen
        val wcOrderStatusModel = WcOrderTestUtils.generateOrderDetail(note = "This is a test note")
        activityTestRule.setOrderDetailWithMockData(wcOrderStatusModel)

        // click on the first order in the list and check if redirected to order detail
        Espresso.onView(ViewMatchers.withId(R.id.ordersList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        // customerNote is empty so Hide Customer Note card
        onView(withId(R.id.orderDetail_customerNote)).check(matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}