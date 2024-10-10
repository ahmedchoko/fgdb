package com.wevioo.fgdb.referenciel.rest.dto;


import com.wevioo.fgdb.referenciel.domain.Country;
import com.wevioo.fgdb.referenciel.service.ReferentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class ReferentialController {

    @Autowired
    private ReferentialService referentialService;

    @GetMapping
    public List<?> getAllCountries() {
        return referentialService.getAllCountries();
    }

    @PostMapping("/findByIds")
    public List<Country> findByIdIn(@RequestBody List<String> ids) {
        return referentialService.findByIdIn(ids);
    }
}
