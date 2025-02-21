package com.lrin.project.controller;

import com.lrin.project.dto.item.ItemDTO;
import com.lrin.project.entity.board.BoardEntity;
import com.lrin.project.entity.item.Item;
import com.lrin.project.repository.board.BoardRepository;
import com.lrin.project.repository.home.HomeRepository;
import com.lrin.project.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;

    @Autowired
    private HomeRepository homeRepository;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<BoardEntity> boardEntityList = this.homeRepository.findBoardRecent();
        List<BoardEntity> latestFive = boardEntityList.stream().limit(5).toList();
        model.addAttribute("cssPath", "home/index");
        model.addAttribute("pageTitle", "메인");
        model.addAttribute("jsPath", "home/index");
        model.addAttribute("boardEntityList", latestFive);
        return "home/index";
    }
    @GetMapping(value = "/price")
    public String price(Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("cssPath", "home/price");
        model.addAttribute("pageTitle", "수거 가격표");
        model.addAttribute("items", items);
        return "home/price";
    }
}