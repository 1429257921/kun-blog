package com.kun.blog.controller;

import com.kun.blog.entity.game.Yang;
import com.kun.common.web.vo.KunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游戏
 *
 * @author gzc
 * @since 2022/10/4 15:25
 **/
@RequiredArgsConstructor
@RequestMapping("/api/game/")
@RestController
public class GameController {


    @GetMapping("jiLeGeJi")
    public KunResult<?> startGame() {
        Yang yang = new Yang();
        yang.setVisible(true);
        return KunResult.ok();
    }
}
