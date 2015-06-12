package com.uphyca.galette;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;

import java.util.Map;

/**
 * Intercept every event before it is executed in order to add additional data.
 */
public interface HitInterceptor {

    interface Provider {
        HitInterceptor getHitInterceptor(String trackerName);
    }

    interface HitFacade<T extends HitFacade> {

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCustomDimension(int, String)
         */
        T setCustomDimension(int index, String dimension);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setAll(Map)
         */
        T setAll(Map<String, String> params);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setPromotionAction(String)
         */
        T setPromotionAction(String action);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setProductAction(ProductAction)
         */
        T setProductAction(ProductAction action);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setNewSession()
         */
        T setNewSession();

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addPromotion(Promotion)
         */
        T addPromotion(Promotion promotion);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCustomMetric(int, float)
         */
        T setCustomMetric(int index, float metric);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCampaignParamsFromUrl(String)
         */
        T setCampaignParamsFromUrl(String utmParams);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#set(String, String)
         */
        T set(String paramName, String paramValue);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addProduct(Product)
         */
        T addProduct(Product product);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setNonInteraction(boolean)
         */
        T setNonInteraction(boolean nonInteraction);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addImpression(Product, String)
         */
        T addImpression(Product product, String impressionList);
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
     */
    interface ScreenViewFacade extends HitFacade<ScreenViewFacade> {
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder
     */
    interface EventFacade extends HitFacade<EventFacade> {

        /**
         * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setCategory(String)
         */
        EventFacade setCategory(String category);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setLabel(String)
         */
        EventFacade setLabel(String label);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setAction(String)
         */
        EventFacade setAction(String action);

        /**
         * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setValue(long)
         */
        EventFacade setValue(long value);
    }

    /**
     * Intercept event.
     *
     * @param event
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder
     */
    void onEvent(EventFacade event);

    /**
     * Intercept screen view.
     *
     * @param screenView
     * @see com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
     */
    void onScreenView(ScreenViewFacade screenView);
}
