package com.lrin.project.repository.item;

import com.lrin.project.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {


}
