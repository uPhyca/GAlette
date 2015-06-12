package com.uphyca.galette;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;

import java.util.Map;

class EventBuilderDelegate implements HitInterceptor.EventFacade {

    private final HitBuilders.EventBuilder builder;

    public EventBuilderDelegate(HitBuilders.EventBuilder builder) {
        this.builder = builder;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setCategory(String)
     */
    @Override
    public HitInterceptor.EventFacade setCategory(String category) {
        builder.setCategory(category);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setLabel(String)
     */
    @Override
    public HitInterceptor.EventFacade setLabel(String label) {
        builder.setLabel(label);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setCustomDimension(int, String)
     */
    @Override
    public HitInterceptor.EventFacade setCustomDimension(int index, String dimension) {
        builder.setCustomDimension(index, dimension);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setAll(Map)
     */
    @Override
    public HitInterceptor.EventFacade setAll(Map<String, String> params) {
        builder.setAll(params);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setPromotionAction(String)
     */
    @Override
    public HitInterceptor.EventFacade setPromotionAction(String action) {
        builder.setPromotionAction(action);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setProductAction(ProductAction)
     */
    @Override
    public HitInterceptor.EventFacade setProductAction(ProductAction action) {
        builder.setProductAction(action);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setValue(long)
     */
    @Override
    public HitInterceptor.EventFacade setValue(long value) {
        builder.setValue(value);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setNewSession()
     */
    @Override
    public HitInterceptor.EventFacade setNewSession() {
        builder.setNewSession();
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#addPromotion(Promotion)
     */
    @Override
    public HitInterceptor.EventFacade addPromotion(Promotion promotion) {
        builder.addPromotion(promotion);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setCustomMetric(int, float)
     */
    @Override
    public HitInterceptor.EventFacade setCustomMetric(int index, float metric) {
        builder.setCustomMetric(index, metric);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setAction(String)
     */
    @Override
    public HitInterceptor.EventFacade setAction(String action) {
        builder.setAction(action);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setCampaignParamsFromUrl(String)
     */
    @Override
    public HitInterceptor.EventFacade setCampaignParamsFromUrl(String utmParams) {
        builder.setCampaignParamsFromUrl(utmParams);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#set(String, String)
     */
    @Override
    public HitInterceptor.EventFacade set(String paramName, String paramValue) {
        builder.set(paramName, paramValue);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#addProduct(Product)
     */
    @Override
    public HitInterceptor.EventFacade addProduct(Product product) {
        builder.addProduct(product);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#setNonInteraction(boolean)
     */
    @Override
    public HitInterceptor.EventFacade setNonInteraction(boolean nonInteraction) {
        builder.setNonInteraction(nonInteraction);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.EventBuilder#addImpression(Product, String)
     */
    @Override
    public HitInterceptor.EventFacade addImpression(Product product, String impressionList) {
        builder.addImpression(product, impressionList);
        return this;
    }
}
