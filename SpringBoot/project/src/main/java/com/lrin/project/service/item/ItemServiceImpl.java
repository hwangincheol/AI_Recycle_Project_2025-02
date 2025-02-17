package com.lrin.project.service.item;

import com.lrin.project.dto.item.ItemDTO;
import com.lrin.project.entity.item.Item;
import com.lrin.project.repository.item.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemDTO> getItems() {
        List<Item> itemList = itemRepository.findAll();

        List<ItemDTO> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemName(item.getItemName());  // 엔티티 필드를 DTO에 설정
            itemDTO.setItemNameKor(item.getItemNameKor());
            itemDTO.setPrice(item.getPrice());

            itemDtoList.add(itemDTO);
        }

        return itemDtoList;  // 모든 Item 객체를 반환
    }

}

