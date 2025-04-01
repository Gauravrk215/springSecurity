package com.realnet.FabricIcard.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.FabricIcard.Entity.PageSource;
import com.realnet.FabricIcard.Repository.PageSourceRepository;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    @Autowired
    private PageSourceRepository pageSourceRepository;

    @PostMapping("/save")
    public String savePageSource(@RequestBody String sourceCode) {
        PageSource pageSource = new PageSource();
        pageSource.setSourceCode(sourceCode);

        // Save to the database
        pageSourceRepository.save(pageSource);

        return "Page source saved successfully!";
    }
}
