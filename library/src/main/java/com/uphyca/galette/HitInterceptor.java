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

        T setCustomDimension(int index, String dimension);

        T setAll(Map<String, String> params);

        T setPromotionAction(String action);

        T setProductAction(ProductAction action);

        T setNewSession();

        T addPromotion(Promotion promotion);

        T setCustomMetric(int index, float metric);

        T setCampaignParamsFromUrl(String utmParams);

        T set(String paramName, String paramValue);

        T addProduct(Product product);

        T setNonInteraction(boolean nonInteraction);

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
