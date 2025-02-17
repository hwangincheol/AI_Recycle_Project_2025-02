package com.lrin.project.config;

import com.lrin.project.entity.item.Item;
import com.lrin.project.repository.item.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final ItemRepository itemRepository;

    public DataInitializer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            if (itemRepository.count() == 0) {
                itemRepository.save(new Item("Oven_Range", "전자레인지", 6000));
                itemRepository.save(new Item("Air_Purifier", "공기청정기", 7000));
                itemRepository.save(new Item("Refrigerator", "냉장고", 10000));
                itemRepository.save(new Item("Printer", "복사기", 9000));
                itemRepository.save(new Item("Washing_Machine", "세탁기", 6000));
                itemRepository.save(new Item("Air_Conditioner", "에어컨", 10000));
                itemRepository.save(new Item("Audio", "오디오", 7000));
                itemRepository.save(new Item("Heater", "전기난로", 5000));
                itemRepository.save(new Item("Desktop_Computer", "컴퓨터본체", 6000));
                itemRepository.save(new Item("Keyboard", "키보드", 3000));
                itemRepository.save(new Item("Monitor", "TV모니터", 7000));
                itemRepository.save(new Item("Rice_Cooker", "전기밥솥", 3000));
                itemRepository.save(new Item("Fan", "선풍기", 3000));
                itemRepository.save(new Item("Vacuum_Cleaner", "청소기", 3000));
                itemRepository.save(new Item("Water_Purifier", "정수기", 4000));
                itemRepository.save(new Item("Blender", "믹서기", 3000));
                itemRepository.save(new Item("Iron", "다리미", 3000));
                itemRepository.save(new Item("Other", "그 외 기타", 3000));
            }
        };
    }
}