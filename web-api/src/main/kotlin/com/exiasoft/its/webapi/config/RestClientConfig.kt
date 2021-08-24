package com.exiasoft.its.webapi.config

import com.exiasoft.its.refdata.endpoint.rest.api.CurrencyApi
import com.exiasoft.its.mktdata.endpoint.rest.api.MarketDataApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestClientConfig {

    @Value("\${application.endPoint.ref-data}")
    lateinit var refDataUrl: String

    @Value("\${application.endPoint.mkt-data}")
    lateinit var mktDataUrl: String

    @Bean
    fun provideCurrencyApi(): CurrencyApi {
        return CurrencyApi(basePath = refDataUrl)
    }

    @Bean
    fun provideMarketDataApi(): MarketDataApi {
        return MarketDataApi(basePath = mktDataUrl)
    }

}