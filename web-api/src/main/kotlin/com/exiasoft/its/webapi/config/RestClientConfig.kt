package com.exiasoft.its.webapi.config

import com.exiasoft.its.mktdata.endpoint.rest.api.MarketDataApi
import com.exiasoft.its.refdata.endpoint.rest.api.CurrencyApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestClientConfig {

    @Value("\${application.endPoint.ref-data}")
    lateinit var refDataUrl: String

    @Value("\${application.endPoint.mkt-data}")
    lateinit var mktDataUrl: String

    @Bean
    fun refDataRestTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun mktDataRestTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun provideCurrencyApi(@Autowired @Qualifier("refDataRestTemplate") restTemplate: RestTemplate): CurrencyApi {
        return CurrencyApi(com.exiasoft.its.refdata.ApiClient(restTemplate).setBasePath(refDataUrl))
    }

    @Bean
    fun provideMarketDataApi(@Autowired @Qualifier("mktDataRestTemplate") restTemplate: RestTemplate): MarketDataApi {
        return MarketDataApi(com.exiasoft.its.mktdata.ApiClient(restTemplate).setBasePath(mktDataUrl))
    }

}