package ru.yandex.practicum.commerce.interactionapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.dto.warehouse.AddressDto;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @PostMapping("/internal/warehouse/assembly")
    void assembly(@RequestParam UUID orderId,
                  @RequestBody Map<UUID, Integer> products);

    @PostMapping("/internal/warehouse/assembly-failed")
    void assemblyFailed(@RequestParam UUID orderId);

    @PostMapping("/internal/warehouse/return")
    void returnProducts(@RequestParam UUID orderId,
                        @RequestBody Map<UUID, Integer> products);

    @GetMapping("/internal/warehouse/address")
    AddressDto getWarehouseAddress();
}

