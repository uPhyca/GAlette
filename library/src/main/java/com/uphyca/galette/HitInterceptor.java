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

    interface ScreenViewFacade extends HitFacade<ScreenViewFacade> {
    }

    interface EventFacade extends HitFacade<EventFacade> {

        EventFacade setCategory(String category);

        EventFacade setLabel(String label);

        EventFacade setAction(String action);

        EventFacade setValue(long value);
    }

    /**
     * Intercept event.
     *
     * @param event
     */
    void onEvent(EventFacade event);

    /**
     * Intercept screen view.
     *
     * @param screenView
     */
    void onScreenView(ScreenViewFacade screenView);
}
