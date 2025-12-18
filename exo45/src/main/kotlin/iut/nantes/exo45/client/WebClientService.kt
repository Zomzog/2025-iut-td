package iut.nantes.exo45.client

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
@Profile("webclient")
class WebClientService(val webClient: WebClient) {
    fun hello(): HelloDto? {
        return webClient.get().uri {
            it.path("/api/v1/hello")
                .queryParam("name", "world")
                .build()
        }
            .retrieve()
            .bodyToMono(HelloDto::class.java)
            .block()
    }

    fun noHello(): HelloDto? {
        return webClient.post().uri {
            it.path("/api/v1/noHello")
                .build()
        }.bodyValue(HelloDto("No hello"))
            .retrieve()
            .bodyToMono(HelloDto::class.java)
            .block()
    }

    fun error() {
        webClient.get().uri {
            it.path("/api/v1/error")
                .build()}
            .retrieve()
            .onStatus({ it.is5xxServerError }, { Mono.error(MyInternalError()) })
            .bodyToMono(HelloDto::class.java)
            .block()
    }
}