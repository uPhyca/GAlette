package com.uphyca.galette;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;

import java.util.Map;

class ScreenViewBuilderDelegate implements HitInterceptor.ScreenViewFacade {

    private final HitBuilders.ScreenViewBuilder builder;

    public ScreenViewBuilderDelegate(HitBuilders.ScreenViewBuilder builder) {
        this.builder = builder;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCustomDimension(int, String)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setCustomDimension(int index, String dimension) {
        builder.setCustomDimension(index, dimension);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setAll(Map)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setAll(Map<String, String> params) {
        builder.setAll(params);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setPromotionAction(String)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setPromotionAction(String action) {
        builder.setPromotionAction(action);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setProductAction(ProductAction)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setProductAction(ProductAction action) {
        builder.setProductAction(action);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setNewSession()
     */
    @Override
    public HitInterceptor.ScreenViewFacade setNewSession() {
        builder.setNewSession();
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addPromotion(Promotion)
     */
    @Override
    public HitInterceptor.ScreenViewFacade addPromotion(Promotion promotion) {
        builder.addPromotion(promotion);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCustomMetric(int, float)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setCustomMetric(int index, float metric) {
        builder.setCustomMetric(index, metric);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setCampaignParamsFromUrl(String)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setCampaignParamsFromUrl(String utmParams) {
        builder.setCampaignParamsFromUrl(utmParams);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#set(String, String)
     */
    @Override
    public HitInterceptor.ScreenViewFacade set(String paramName, String paramValue) {
        builder.set(paramName, paramValue);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addProduct(Product)
     */
    @Override
    public HitInterceptor.ScreenViewFacade addProduct(Product product) {
        builder.addProduct(product);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#setNonInteraction(boolean)
     */
    @Override
    public HitInterceptor.ScreenViewFacade setNonInteraction(boolean nonInteraction) {
        builder.setNonInteraction(nonInteraction);
        return this;
    }

    /**
     * @see com.google.android.gms.analytics.HitBuilders.HitBuilder#addImpression(Product, String)
     */
    @Override
    public HitInterceptor.ScreenViewFacade addImpression(Product product, String impressionList) {
        builder.addImpression(product, impressionList);
        return this;
    }
}
